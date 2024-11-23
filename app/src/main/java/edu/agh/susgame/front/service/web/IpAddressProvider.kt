package edu.agh.susgame.front.service.web

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.net.InetAddress

class IpAddressProvider {
    private val _isIpAddressDefined = MutableStateFlow(false)
    val isIpAddressDefined: StateFlow<Boolean> get() = _isIpAddressDefined

    private var currentIpAddress: String? = null

    fun isIpAddressDefined(): Boolean = currentIpAddress != null

    fun getCurrentIpAddress(): String? = currentIpAddress

    fun setIpAddress(newValue: String) {
        if (isValidIPv4Address(newValue)) {
            currentIpAddress = newValue
            _isIpAddressDefined.value = true
        }
    }

    companion object {
        fun isValidIPv4Address(ip: String): Boolean = try {
            val address = InetAddress.getByName(ip)
            address.hostAddress == ip && address is java.net.Inet4Address
        } catch (e: Exception) {
            false
        }
    }
}
