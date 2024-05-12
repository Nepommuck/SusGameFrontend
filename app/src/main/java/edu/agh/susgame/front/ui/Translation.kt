package edu.agh.susgame.front.ui

object Translation {
    const val GAME_TITLE = "SusGame"

    object Button {
        const val GO_BACK = "Wróć"
        const val LEAVE = "Opuść"
        const val JOIN = "Dołącz"
        const val PLAY = "Graj"
        const val CREATE = "Stwórz"
    }

    object Toasts {
        const val CREATE_NO_GAME_NAME = "Podaj nazwę gry!"
        const val CREATE_SUCCESS = "Pomyślnie stworzono grę!"
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
    object CreateGame{
        const val ENTER_GAME_NAME = "Nazwa gry"
        const val ENTER_GAME_PIN = "PIN gry"
        const val AMOUNT_OF_PLAYERS = "Liczba graczy:"
        const val GAME_TIME = "Czas gry:"
        const val MINUTES = " minuty "
        const val DEFAULT_GAME_NAME = "Uqsz's game"
    }

    object Game {
        const val MAP = "Mapa"
        const val COMPUTER = "Komputer"
    }
}