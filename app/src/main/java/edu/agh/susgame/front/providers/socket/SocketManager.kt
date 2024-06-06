package edu.agh.susgame.front.providers.socket

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okio.ByteString

class WebSocketManager {
    private val _messagesFlow = MutableSharedFlow<String>()
    private val _bytesFlow = MutableSharedFlow<ByteString>()

    val messagesFlow: SharedFlow<String> = _messagesFlow.asSharedFlow()
    val bytesFlow: SharedFlow<ByteString> = _bytesFlow.asSharedFlow()

    fun onMessageReceived(message: String) {
        CoroutineScope(Dispatchers.Main).launch {
            _messagesFlow.emit(message)
        }
    }

    fun onBytesReceived(bytes: ByteString) {
        CoroutineScope(Dispatchers.Main).launch {
            _bytesFlow.emit(bytes)
        }
    }
}
