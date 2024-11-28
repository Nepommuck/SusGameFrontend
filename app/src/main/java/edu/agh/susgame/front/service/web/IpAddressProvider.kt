package edu.agh.susgame.front.service.web

import edu.agh.susgame.front.config.AppConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.net.InetAddress

class IpAddressProvider {
    private var currentIpAddress: String? = AppConfig.webConfig.defaultIpAddress

    private val _isIpAddressDefined = MutableStateFlow(currentIpAddress != null)
    val isIpAddressDefined: StateFlow<Boolean> get() = _isIpAddressDefined

    fun isIpAddressDefined(): Boolean = currentIpAddress != null

    fun getCurrentIpAddress(): String? = currentIpAddress

    fun setIpAddress(newValue: String) {
        if (isValidIPv4Address(newValue)) {
            currentIpAddress = newValue
            _isIpAddressDefined.value = true
        }
    }

    fun setEmptyIpAddress() {
        currentIpAddress = null
        _isIpAddressDefined.value = false
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
