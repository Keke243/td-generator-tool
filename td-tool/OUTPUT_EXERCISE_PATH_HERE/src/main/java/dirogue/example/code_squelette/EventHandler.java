package dirogue.example.code_squelette;

@FunctionalInterface
public interface EventHandler {
	public void handle(String cmd, String... args);
}
