package edu.agh.susgame.front.ui

import edu.agh.susgame.front.model.game.LobbyId

object Translation {
    const val GAME_TITLE = "SusGame"

    object Button {
        const val GO_BACK = "Wróć"
        const val BACK_TO_MAIN_MENU = "Wróć do menu głównego"
        const val LEAVE = "Opuść"
        const val JOIN = "Dołącz"
        const val PLAY = "Graj"
        const val CREATE = "Stwórz"
        const val LOADING = "Ładowanie"
    }


    object Menu {
        object SearchGame {
            const val JOIN_GAME = "Dołącz do gry"
            const val FIND_GAME = "Znajdź grę"
            fun nPlayersAwaiting(n: Int): String {
                require(n >= 0) { "Number of players can't be negative, but was: $n" }

                return "Oczekuje $n ${if (n == 1) "gracz" else "graczy"}"
            }
        }

        const val CREATE_GAME = "Stwórz nową grę"
    }

    object CreateGame {
        const val ENTER_GAME_NAME = "Nazwa gry"
        const val ENTER_GAME_PIN = "PIN gry"
        const val AMOUNT_OF_PLAYERS = "Liczba graczy"
        const val GAME_TIME = "Czas gry"
        const val MINUTES = "minut"
        const val DEFAULT_GAME_NAME = "default"
        const val CREATE_NO_GAME_NAME = "Podaj nazwę gry!"
        const val CREATE_SUCCESS = "Pomyślnie stworzono grę!"
        const val CREATE_NAME_ALREADY_EXISTS = "Gra z taką nazwą już istnieje"
        const val CREATE_OTHER_ERROR = "Wystąpił niespodziewany błąd"
    }

    object Game {
        const val MAP = "Mapa"
        const val COMPUTER = "Komputer"
        const val BUFFER_SIZE = "Rozmiar bufora"
        const val BUFFER_CURRENT_PACKETS = "Liczba pakietów w buforze"
        const val PACKETS_TO_SEND = "Liczba pakietów do wysłania"
        const val PACKETS_TO_WIN = "Liczba pakietów do wygranej"
        const val PACKETS_RECEIVED = "Liczba otrzymanych pakietów"
        const val ROUTER = "Router"
        const val HOST = "Host"
        const val SERVER = "Serwer"
    }

    object Error {
        private const val ERROR = "BŁĄD"

        const val UnexpectedError = "Wystąpił niespodziewany błąd"

        fun failedToLoadGame(lobbyId: LobbyId) =
            "$ERROR: Nie udało się wczytać gry: ${lobbyId.value}"
    }
}