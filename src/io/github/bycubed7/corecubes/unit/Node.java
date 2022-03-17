package io.github.bycubed7.corecubes.unit;

import java.util.List;

public class Node<T> {
	private T value;
	private List<Node<T>> children;

	public Node(T _value) {
		setValue(_value);
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public int size() {
		return children.size();
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public boolean contains(T node) {
		return children.contains(node);
	}

	public Node<T> get(String byValue) {
		return children.get(0);
	}

	public boolean add(Node<T> node) {
		return children.add(node);
	}

	public boolean add(T nodeValue) {
		return children.add(new Node<T>(nodeValue));
	}

	public boolean remove(Node<T> node) {
		return children.remove(node);
	}

	public void clear() {
		children.clear();
	}
}
