package ca.umontreal.tdtool;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Génère un énoncé étudiant (FR) au format TXT et/ou PDF, contenant les consignes
 * et la liste des méthodes rendues volontairement incomplètes.
 *
 * Compatible PDFBox 3.x.
 */
public final class StudentHandout {

    private StudentHandout() {}

    /** Représente une méthode rendue incomplète (stub). */
    public static class StubbedMethod {
        public String id;                 // ex: ca.umontreal.library.Library#addBook(Book)
        public String cut;                // "full" | "partial"
        public Integer keepStatements;    // utilisé si cut=partial

        public StubbedMethod() {}

        public StubbedMethod(String id, String cut, Integer keepStatements) {
            this.id = id;
            this.cut = cut;
            this.keepStatements = keepStatements;
        }
    }

    /**
     * Génère l'énoncé étudiant dans le répertoire du projet "exercice".
     * - TXT et/ou PDF selon cfg.generateStudentTxt / cfg.generateStudentPdf
     * - Nom de base: cfg.studentHandoutBaseName (défaut: "ENONCE_TD")
     *
     * Si cfg est null, génère TXT + PDF avec base "ENONCE_TD".
     */
    public static void generate(Path outputProjectRoot, Config cfg, List<StubbedMethod> methods) throws IOException {
        if (outputProjectRoot == null) {
            throw new IllegalArgumentException("outputProjectRoot is null");
        }
        if (methods == null) methods = Collections.emptyList();

        boolean genTxt = true;
        boolean genPdf = true;
        String baseName = "ENONCE_TD";

        if (cfg != null) {
            if (cfg.generateStudentTxt != null) genTxt = cfg.generateStudentTxt;
            if (cfg.generateStudentPdf != null) genPdf = cfg.generateStudentPdf;
            if (cfg.studentHandoutBaseName != null && !cfg.studentHandoutBaseName.isBlank()) {
                baseName = cfg.studentHandoutBaseName.trim();
            }
        }

        List<String> lines = buildFrenchHandoutLines(methods);

        if (genTxt) {
            Path txt = outputProjectRoot.resolve(baseName + ".txt");
            Files.write(txt, lines, StandardCharsets.UTF_8);
        }

        if (genPdf) {
            Path pdf = outputProjectRoot.resolve(baseName + ".pdf");
            writePdf(pdf, lines);
        }
    }

    private static List<String> buildFrenchHandoutLines(List<StubbedMethod> methods) {
        List<String> lines = new ArrayList<>();

        lines.add("TRAVAIL DIRIGÉ (TD) — Tests unitaires & ré-implémentation");
        lines.add("Date : " + LocalDate.now());
        lines.add("");

        lines.add("Objectif");
        lines.add("--------");
        lines.add("Dans ce projet, certaines méthodes ont été volontairement rendues incomplètes.");
        lines.add("Votre travail consiste à compléter ces méthodes afin que tous les tests unitaires passent.");
        lines.add("");

        lines.add("Consignes");
        lines.add("---------");
        lines.add("1) Ouvrez le projet et exécutez les tests : mvn test");
        lines.add("2) Lisez les tests pour comprendre le comportement attendu.");
        lines.add("3) Implémentez / corrigez les méthodes listées ci-dessous.");
        lines.add("4) Relancez mvn test jusqu'à obtenir un succès.");
        lines.add("");

        lines.add("Contraintes");
        lines.add("-----------");
        lines.add("- Ne modifiez pas les tests.");
        lines.add("- Vous pouvez ajouter du code dans src/main/java si nécessaire.");
        lines.add("- Gardez le comportement cohérent avec les tests.");
        lines.add("");

        lines.add("Méthodes à compléter");
        lines.add("--------------------");
        if (methods == null || methods.isEmpty()) {
            lines.add("(Aucune méthode listée)");
        } else {
            for (StubbedMethod m : methods) {
                lines.add(formatMethodLine(m));
            }
        }
        lines.add("");

        lines.add("Rendu attendu");
        lines.add("-------------");
        lines.add("Un projet où tous les tests passent (mvn test).");
        lines.add("");

        return lines;
    }

    private static String formatMethodLine(StubbedMethod m) {
        String id = (m == null || m.id == null) ? "(id manquant)" : m.id.trim();
        String cut = (m == null || m.cut == null) ? "full" : m.cut.trim().toLowerCase();

        if ("partial".equals(cut)) {
            int keep = (m == null || m.keepStatements == null) ? 1 : Math.max(0, m.keepStatements);
            return "- " + id + "  (coupure : partielle, " + keep + " instruction(s) conservée(s))";
        }
        return "- " + id + "  (coupure : totale)";
    }

    private static void writePdf(Path pdfPath, List<String> logicalLines) throws IOException {
        if (logicalLines == null) logicalLines = List.of();

        final PDFont font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        final PDFont fontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

        final float fontSize = 11f;
        final float titleSize = 14f;

        final float margin = 54f;
        final float leadingNormal = 1.35f * fontSize;
        final float leadingTitle = 1.35f * titleSize;

        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.LETTER);
            doc.addPage(page);

            PDRectangle media = page.getMediaBox();
            float pageWidth = media.getWidth();
            float pageHeight = media.getHeight();

            float y = pageHeight - margin;

            PDPageContentStream cs = new PDPageContentStream(doc, page);
            try {
                for (int i = 0; i < logicalLines.size(); i++) {
                    String raw = logicalLines.get(i);
                    String safe = safePdfText(raw);

                    boolean isTitle = (i == 0);
                    PDFont currentFont = isTitle ? fontBold : font;
                    float currentSize = isTitle ? titleSize : fontSize;
                    float currentLeading = isTitle ? leadingTitle : leadingNormal;

                    List<String> wrapped = wrapLine(safe, currentFont, currentSize, pageWidth - 2 * margin);

                    for (String line : wrapped) {
                        if (y - currentLeading < margin) {
                            cs.close();

                            page = new PDPage(PDRectangle.LETTER);
                            doc.addPage(page);

                            media = page.getMediaBox();
                            pageWidth = media.getWidth();
                            pageHeight = media.getHeight();
                            y = pageHeight - margin;

                            cs = new PDPageContentStream(doc, page);
                        }

                        cs.beginText();
                        cs.setFont(currentFont, currentSize);
                        cs.newLineAtOffset(margin, y);
                        cs.showText(line);
                        cs.endText();

                        y -= currentLeading;
                    }
                }
            } finally {
                cs.close();
            }

            doc.save(pdfPath.toFile());
        }
    }

    private static String safePdfText(String s) {
        if (s == null) return "";
        return s.replace("\t", "    ")
                .replace("\r", "")
                .replace("\u0000", "");
    }

    private static List<String> wrapLine(String line, PDFont font, float fontSize, float maxWidth) throws IOException {
        if (line == null) return List.of("");
        if (line.isEmpty()) return List.of("");

        String[] words = line.split("\\s+");
        List<String> out = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        for (String w : words) {
            if (w.isEmpty()) continue;

            if (stringWidth(font, fontSize, w) > maxWidth) {
                if (current.length() > 0) {
                    out.add(current.toString());
                    current.setLength(0);
                }
                out.addAll(breakLongToken(w, font, fontSize, maxWidth));
                continue;
            }

            if (current.length() == 0) {
                current.append(w);
            } else {
                String candidate = current + " " + w;
                if (stringWidth(font, fontSize, candidate) <= maxWidth) {
                    current.append(" ").append(w);
                } else {
                    out.add(current.toString());
                    current.setLength(0);
                    current.append(w);
                }
            }
        }

        if (current.length() > 0) out.add(current.toString());
        return out;
    }

    private static List<String> breakLongToken(String token, PDFont font, float fontSize, float maxWidth) throws IOException {
        List<String> parts = new ArrayList<>();
        if (token == null || token.isEmpty()) {
            parts.add("");
            return parts;
        }

        StringBuilder chunk = new StringBuilder();
        for (int i = 0; i < token.length(); i++) {
            char c = token.charAt(i);
            chunk.append(c);

            if (stringWidth(font, fontSize, chunk.toString()) > maxWidth) {
                chunk.setLength(Math.max(0, chunk.length() - 1));

                if (chunk.length() == 0) {
                    parts.add(String.valueOf(c));
                } else {
                    parts.add(chunk.toString());
                    chunk.setLength(0);
                    chunk.append(c);
                }
            }
        }

        if (chunk.length() > 0) parts.add(chunk.toString());
        return parts;
    }

    private static float stringWidth(PDFont font, float fontSize, String s) throws IOException {
        return (font.getStringWidth(s) / 1000f) * fontSize;
    }
}
