package dev.linl33.adventofcode.lib;

import java.util.StringJoiner;

public class AdventLinkedListNode<T> {
  private final T value;
  private AdventLinkedListNode<T> next;
  private AdventLinkedListNode<T> prev;

  public T getValue() {
    return value;
  }

  public AdventLinkedListNode<T> getNext() {
    return next;
  }

  public void setNext(AdventLinkedListNode<T> next) {
    this.next = next;
  }

  public AdventLinkedListNode<T> getPrev() {
    return prev;
  }

  public void setPrev(AdventLinkedListNode<T> prev) {
    this.prev = prev;
  }

  public AdventLinkedListNode(T value) {
    this.value = value;
    this.next = this;
    this.prev = this;
  }

  public AdventLinkedListNode<T> nextN(int n) {
    var target = this;
    while (n != 0) {
      if (n > 0) {
        target = target.getNext();
        n--;
      } else {
        target = target.getPrev();
        n++;
      }
    }

    return target;
  }

  public AdventLinkedListNode<T> linkBefore(AdventLinkedListNode<T> node) {
    getPrev().setNext(node);
    node.setPrev(getPrev());

    node.setNext(this);
    setPrev(node);

    return node;
  }

  public AdventLinkedListNode<T> linkAfter(AdventLinkedListNode<T> node) {
    getNext().setPrev(node);
    node.setNext(getNext());

    node.setPrev(this);
    setNext(node);

    return node;
  }

  public AdventLinkedListNode<T> unlink() {
    if (getPrev() != this) {
      var prevNewNext = getNext();
      if (prevNewNext == this) {
        prevNewNext = getPrev();
      }

      getPrev().setNext(prevNewNext);
      setPrev(this);
    }

    if (getNext() != this) {
      var nextNewPrev = getPrev();
      if (nextNewPrev == this) {
        nextNewPrev = getNext();
      }

      getNext().setPrev(nextNewPrev);
      setNext(this);
    }

    return this;
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(" ");
    joiner.add(getValue().toString());

    var pointer = getNext();
    while (pointer != this) {
      joiner.add(pointer.getValue().toString());
      pointer = pointer.getNext();
    }

    return joiner.toString();
  }
}
