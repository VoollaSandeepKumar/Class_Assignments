package waterFlow;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

public class GraphNodeComparator implements Comparator<GraphNode> {

	public int compare(GraphNode o1, GraphNode o2) {

		int cost1 = o1.costToReachNode;
		int cost2 = o2.costToReachNode;
		int nodeNameComparedValue = 0;
		if (cost1 == cost2)
		{
			nodeNameComparedValue=o1.nodeName.compareTo(o2.nodeName);
			if(nodeNameComparedValue==0)
			return 0;
			if(nodeNameComparedValue>0)
			return 1;
			if(nodeNameComparedValue<0)
			return -1;
			
			
		}
			
		if (cost1 > cost2)
			return 1;
		else
			return -1;
	}

	public Comparator<GraphNode> reversed() {
		return null;
	}

	public Comparator<GraphNode> thenComparing(
			Comparator<? super GraphNode> other) {
		return null;
	}

	public <U> Comparator<GraphNode> thenComparing(
			Function<? super GraphNode, ? extends U> keyExtractor,
			Comparator<? super U> keyComparator) {
		return null;
	}

	public <U extends Comparable<? super U>> Comparator<GraphNode> thenComparing(
			Function<? super GraphNode, ? extends U> keyExtractor) {
		// TODO Auto-generated method stub
		return null;
	}

	public Comparator<GraphNode> thenComparingInt(
			ToIntFunction<? super GraphNode> keyExtractor) {
		return null;
	}

	public Comparator<GraphNode> thenComparingLong(
			ToLongFunction<? super GraphNode> keyExtractor) {
		return null;
	}

	public Comparator<GraphNode> thenComparingDouble(
			ToDoubleFunction<? super GraphNode> keyExtractor) {
		return null;
	}

	public static <T extends Comparable<? super T>> Comparator<T> reverseOrder() {
		return null;
	}

	public static <T extends Comparable<? super T>> Comparator<T> naturalOrder() {
		return null;
	}

	public static <T> Comparator<T> nullsFirst(Comparator<? super T> comparator) {
		return null;
	}

	public static <T> Comparator<T> nullsLast(Comparator<? super T> comparator) {
		return null;
	}

	public static <T, U> Comparator<T> comparing(
			Function<? super T, ? extends U> keyExtractor,
			Comparator<? super U> keyComparator) {
		return null;
	}

	public static <T, U extends Comparable<? super U>> Comparator<T> comparing(
			Function<? super T, ? extends U> keyExtractor) {
		return null;
	}

	public static <T> Comparator<T> comparingInt(
			ToIntFunction<? super T> keyExtractor) {
		return null;
	}

	public static <T> Comparator<T> comparingLong(
			ToLongFunction<? super T> keyExtractor) {
		return null;
	}

	public static <T> Comparator<T> comparingDouble(
			ToDoubleFunction<? super T> keyExtractor) {
		return null;
	}

}
