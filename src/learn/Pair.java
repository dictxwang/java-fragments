package learn;

public class Pair<F, S> {

	private F first;
	private S second;
	
	public F getFirst() {
		return this.first;
	}
	
	public S getSecond() {
		return this.second;
	}
	
	private Pair() {
	}
	
	public static <F, S> Pair<F, S> makePair(F f, S s) {
		Pair<F, S> result = new Pair<F, S>();
		result.first = f;
		result.second = s;
		return result;
	}
}
