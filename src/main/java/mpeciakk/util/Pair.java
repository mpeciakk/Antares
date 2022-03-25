package mpeciakk.util;

import java.util.Objects;

public record Pair<F, S>(F first, S second) {

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof final Pair<?, ?> other)) {
            return false;
        }
        return Objects.equals(first, other.first) && Objects.equals(second, other.second);
    }
}
