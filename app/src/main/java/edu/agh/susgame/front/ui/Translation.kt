package edu.agh.susgame.front.ui

object Translation {
    const val GAME_TITLE = "SusGame"

    object Button {
        const val GO_BACK = "Wróć"
        const val LEAVE = "Opuść"
        const val JOIN = "Dołącz"
        const val PLAY = "Graj"
    }

    object Menu {
        object SearchGame {
            const val JOIN_GAME = "Dołącz do gry"
            const val FIND_GAME = "Znajdź do gry"
            fun nPlayersAwaiting(n: Int): String {
                require(n >= 0) { "Number of players can't be negative, but was: $n" }

                return "Oczekuje $n ${if (n == 1) "gracz" else "graczy"}"
            }
        }

        const val CREATE_GAME = "Stwórz nową grę"
    }

    object Game {
        const val MAP = "Mapa"
        const val COMPUTER = "Komputer"
    }
}