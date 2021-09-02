package com.skyyo.groupingandreplytonotifications

import kotlinx.coroutines.channels.Channel

object NotificationEventDispatcher {
    val emitter = Channel<NotificationModel>(Channel.UNLIMITED)

    fun emit(notification: NotificationModel) = emitter.trySend(notification)
}