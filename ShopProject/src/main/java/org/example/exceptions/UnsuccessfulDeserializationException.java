package org.example.exceptions;

public class UnsuccessfulDeserializationException extends RuntimeException {
  public UnsuccessfulDeserializationException(String message) {
    super(message);
  }
}
