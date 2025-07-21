package io.schinzel.web_blocks.test_utils

import java.io.IOException
import java.net.ServerSocket
import kotlin.random.Random

/**
 * Utility class for test port allocation to avoid conflicts when running tests in parallel.
 * 
 * Created by Claude Code for WebBlocks framework.
 */
object PortUtil {
    
    private const val MIN_PORT = 49152
    private const val MAX_PORT = 65535
    private const val MAX_ATTEMPTS = 10

    /**
     * Finds an available port in the ephemeral port range (49152-65535).
     * 
     * @return An available port number
     * @throws IllegalStateException if no available port is found after MAX_ATTEMPTS tries
     */
    fun findAvailablePort(): Int {
        repeat(MAX_ATTEMPTS) {
            val port = Random.nextInt(MIN_PORT, MAX_PORT + 1)
            if (isPortAvailable(port)) {
                return port
            }
        }
        throw IllegalStateException("Could not find an available port after $MAX_ATTEMPTS attempts")
    }

    /**
     * Checks if a port is available for binding.
     * 
     * @param port The port to check
     * @return true if the port is available, false otherwise
     */
    private fun isPortAvailable(port: Int): Boolean =
        try {
            ServerSocket(port).use { true }
        } catch (_: IOException) {
            false
        }
}