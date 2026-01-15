
import React from 'react';

const App: React.FC = () => {
  const codeFiles = [
    "pom.xml",
    "src/main/java/td/analyzer/Main.java",
    "src/main/java/td/analyzer/config/AnalyzerConfig.java",
    "src/main/java/td/analyzer/model/MethodInfo.java",
    "src/main/java/td/analyzer/scan/ProjectScanner.java",
    "src/main/java/td/analyzer/score/HeuristicScorer.java",
    "src/main/java/td/analyzer/score/ComplexityScorer.java",
    "src/main/java/td/analyzer/score/DependencyScorer.java",
    "src/main/java/td/analyzer/score/TestSignalScorer.java",
    "src/main/java/td/analyzer/output/YamlWriter.java"
  ];

  return (
    <div className="min-h-screen p-8">
      <header className="mb-12">
        <h1 className="text-4xl font-bold text-indigo-700 mb-2">TD-Analyzer Project</h1>
        <p className="text-lg text-slate-600">A Senior Java tool to automatically select methods for code-cutting exercises.</p>
      </header>

      <section className="grid grid-cols-1 md:grid-cols-2 gap-8">
        <div className="bg-white p-6 rounded-xl shadow-md border border-slate-200">
          <h2 className="text-xl font-semibold mb-4 flex items-center gap-2">
            <span className="p-2 bg-indigo-100 rounded-lg text-indigo-600">📦</span>
            Project Structure
          </h2>
          <ul className="space-y-2 font-mono text-sm">
            {codeFiles.map(file => (
              <li key={file} className="flex items-center gap-2">
                <span className="text-slate-400">📄</span> {file}
              </li>
            ))}
          </ul>
        </div>

        <div className="bg-white p-6 rounded-xl shadow-md border border-slate-200">
          <h2 className="text-xl font-semibold mb-4 flex items-center gap-2">
            <span className="p-2 bg-green-100 rounded-lg text-green-600">🚀</span>
            How to run
          </h2>
          <div className="bg-slate-900 text-slate-300 p-4 rounded-lg overflow-x-auto text-xs space-y-3">
            <p># 1. Build the project</p>
            <code className="block text-white">mvn clean package</code>
            <p># 2. Run the analyzer on a target project</p>
            <code className="block text-white">
              java -jar target/td-analyzer-1.0.0-jar-with-dependencies.jar \<br/>
              &nbsp;&nbsp;--input "C:\dev\my-java-solution" \<br/>
              &nbsp;&nbsp;--output "C:\dev\my-java-exercise" \<br/>
              &nbsp;&nbsp;--top 15 \<br/>
              &nbsp;&nbsp;--mode business \<br/>
              &nbsp;&nbsp;--out-yaml td-config.yaml
            </code>
          </div>
        </div>
      </section>

      <section className="mt-8 bg-indigo-900 text-white p-8 rounded-xl shadow-lg">
        <h2 className="text-2xl font-bold mb-4">Core Algorithm Overview</h2>
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
          <div className="bg-white/10 p-4 rounded-lg">
            <h3 className="font-bold text-indigo-300">Heuristics (H)</h3>
            <p className="text-sm opacity-90">Measures pedagogical value based on statements and control flow.</p>
          </div>
          <div className="bg-white/10 p-4 rounded-lg">
            <h3 className="font-bold text-indigo-300">Complexity (C)</h3>
            <p className="text-sm opacity-90">Normalized cyclomatic complexity using conditional branch counting.</p>
          </div>
          <div className="bg-white/10 p-4 rounded-lg">
            <h3 className="font-bold text-indigo-300">Dependencies (D)</h3>
            <p className="text-sm opacity-90">Uses a bell-curve function on Fan-In to favor moderately central methods.</p>
          </div>
          <div className="bg-white/10 p-4 rounded-lg">
            <h3 className="font-bold text-indigo-300">Test Signal (T)</h3>
            <p className="text-sm opacity-90">Cross-references method IDs with src/test/java to detect usage.</p>
          </div>
        </div>
      </section>
    </div>
  );
};

export default App;
