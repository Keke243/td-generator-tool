package td.analyzer.model;

import java.nio.file.Path;
import java.util.Objects;

public class MethodInfo {

    private String id;
    private String className;
    private String methodName;
    private String paramTypes;
    private Path filePath;

    private int statementCount;
    private int branchCount;
    private int returnCount;
    private int loopCount;
    private int catchCount;

    private int fanIn;
    private int fanOut;

    private int testRefs;

    private double heuristicsScore;
    private double complexityScore;
    private double dependencyScore;
    private double testSignalScore;

    private double globalScore;

    public MethodInfo() {}

    public MethodInfo(String id) {
        this.id = id;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getMethodName() { return methodName; }
    public void setMethodName(String methodName) { this.methodName = methodName; }

    public String getParamTypes() { return paramTypes; }
    public void setParamTypes(String paramTypes) { this.paramTypes = paramTypes; }

    public Path getFilePath() { return filePath; }
    public void setFilePath(Path filePath) { this.filePath = filePath; }

    public int getStatementCount() { return statementCount; }
    public void setStatementCount(int statementCount) { this.statementCount = statementCount; }

    public int getBranchCount() { return branchCount; }
    public void setBranchCount(int branchCount) { this.branchCount = branchCount; }

    public int getReturnCount() { return returnCount; }
    public void setReturnCount(int returnCount) { this.returnCount = returnCount; }

    public int getLoopCount() { return loopCount; }
    public void setLoopCount(int loopCount) { this.loopCount = loopCount; }

    public int getCatchCount() { return catchCount; }
    public void setCatchCount(int catchCount) { this.catchCount = catchCount; }

    public int getFanIn() { return fanIn; }
    public void setFanIn(int fanIn) { this.fanIn = fanIn; }

    public int getFanOut() { return fanOut; }
    public void setFanOut(int fanOut) { this.fanOut = fanOut; }

    public int getTestRefs() { return testRefs; }
    public void setTestRefs(int testRefs) { this.testRefs = testRefs; }

    public double getHeuristicsScore() { return heuristicsScore; }
    public void setHeuristicsScore(double heuristicsScore) { this.heuristicsScore = heuristicsScore; }

    public double getComplexityScore() { return complexityScore; }
    public void setComplexityScore(double complexityScore) { this.complexityScore = complexityScore; }

    public double getDependencyScore() { return dependencyScore; }
    public void setDependencyScore(double dependencyScore) { this.dependencyScore = dependencyScore; }

    public double getTestSignalScore() { return testSignalScore; }
    public void setTestSignalScore(double testSignalScore) { this.testSignalScore = testSignalScore; }

    public double getGlobalScore() { return globalScore; }
    public void setGlobalScore(double globalScore) { this.globalScore = globalScore; }

    // --- Compatibilité avec les autres classes ---

    public String getPrettyId() {
        return id;
    }

    public void setPrettyId(String prettyId) {
        this.id = prettyId;
    }

    public double totalScore() {
        if (globalScore > 0) {
            return globalScore;
        }
        double sum = heuristicsScore + complexityScore + dependencyScore + testSignalScore;
        return sum / 4.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MethodInfo)) return false;
        MethodInfo that = (MethodInfo) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id != null ? id : super.toString();
    }
}
