package dto;

import java.util.Objects;

public class CharacterPair {
    private char first;
    private char second;

    public CharacterPair(char first, char second) {
        this.first = first;
        this.second = second;
    }

    public char getFirst() {
        return first;
    }

    public char getSecond() {
        return second;
    }

    public void setFirst(char first) {
        this.first = first;
    }

    public void setSecond(char second) {
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CharacterPair that = (CharacterPair) o;
        return first == that.first && second == that.second;
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return "{ " + first + " , " + second + " }";
    }
}
