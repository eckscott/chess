package model;

import java.util.Collection;

public record ListGamesResponse(Collection<SimpleGameData> games) {
}
