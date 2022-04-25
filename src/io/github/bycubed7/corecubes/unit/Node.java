package io.github.bycubed7.corecubes.unit;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Node<T> implements Iterable<Node<T>> {
	public T data;
	public Node<T> parent;
	public List<Node<T>> children;

	public boolean isRoot() {
		return parent == null;
	}

	public boolean isLeaf() {
		return children.size() == 0;
	}

	private List<Node<T>> elementsIndex;

	public Node(T data) {
		this.data = data;
		this.children = new LinkedList<Node<T>>();
		this.elementsIndex = new LinkedList<Node<T>>();
		this.elementsIndex.add(this);
	}

	public Node<T> addChild(T child) {
		Node<T> childNode = new Node<T>(child);
		childNode.parent = this;
		this.children.add(childNode);
		this.registerChildForSearch(childNode);
		return childNode;
	}

	public int getLevel() {
		if (this.isRoot())
			return 0;
		else
			return parent.getLevel() + 1;
	}

	private void registerChildForSearch(Node<T> node) {
		elementsIndex.add(node);
		if (parent != null)
			parent.registerChildForSearch(node);
	}

	public Node<T> findNode(Comparable<T> cmp) {
		for (Node<T> element : this.elementsIndex) {
			T elData = element.data;
			if (cmp.compareTo(elData) == 0)
				return element;
		}

		return null;
	}

	@Override
	public String toString() {
		return data != null ? data.toString() : "[data null]";
	}

	@Override
	public Iterator<Node<T>> iterator() {
		NodeIter<T> iter = new NodeIter<T>(this);
		return iter;
	}
}
