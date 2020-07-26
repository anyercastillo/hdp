package com.anyer.bluetooth

class CharacteristicSubscribeException(message: String) : Exception(message) {
    override fun equals(other: Any?): Boolean {
        if (other !is CharacteristicSubscribeException) return false

        return message.equals(other.message)
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}