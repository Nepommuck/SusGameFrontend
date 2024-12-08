package edu.agh.susgame.front.gui.components.common.util

import edu.agh.susgame.dto.rest.model.LobbyId

object Translation {
    object Button {
        const val GO_BACK = "Wróć"
        const val EDIT = "Edytuj"
        const val ACCEPT = "Zatwierdź"
        const val BACK_TO_MAIN_MENU = "Wróć do menu głównego"
        const val LEAVE = "Opuść"
        const val JOIN = "Dołącz"
        const val PLAY = "Rozpocznij gre!"
        const val CREATE = "Stwórz"
        const val LOADING = "Ładowanie"
        const val SEND = "Wyślij"
    }

    object Menu {
        const val GAME_TITLE = "CyberSurfers"
        const val JOIN_GAME = "Dołącz do gry"
        const val CREATE_GAME = "Stwórz nową grę"
        const val IP_ADDRESS = "Adres IP serwera"
    }

    object Lobby {
        const val FINDING_GAMES = "Szukanie gier..."
        const val CHOOSE_GAME = "Wybierz grę"
        const val CHOOSE_NICKNAME = "Podaj nick"
        const val NICKNAME_ERROR_MESSAGE = "Nick nie może zawierać spacji"

        fun nPlayersAwaiting(n: Int): String {
            require(n >= 0) { "Number of players can't be negative, but was: $n" }

            return "Oczekuje $n ${if (n == 1) "gracz" else "graczy"}"
        }
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
        const val YOU_WON = "Wygrałeś!"
        const val YOU_LOST = "Przegrałeś!"
        const val CHAT = "Chat"
        const val STATE = "Stan"
        const val SHUTDOWN = "Wyłączony"
        const val RUNNING = "Działajacy"
        const val BUFFER_STATE = "Stan bufora"
        const val UPGRADE_COST = "Koszt ulepszenia"
        const val RECEIVED_DATA = "Otrzymane dane"
        const val PACKETS_TO_SEND = "Wysyłane pakiety"
        const val TOKENS = "Tokeny"
        const val ROUTER = "Router"
        const val HOST = "Host"
        const val SERVER = "Serwer"
        const val ACCEPT_PATH = "Zatwierdź ścieżkę"
        const val ABORT_PATH = "Anuluj ścieżkę"
    }

    object Error {
        private const val ERROR = "BŁĄD"

        const val UNEXPECTED_ERROR = "Wystąpił niespodziewany błąd"

        fun failedToLoadGame(lobbyId: LobbyId) =
            "$ERROR: Nie udało się wczytać gry: ${lobbyId.value}"
    }
}
