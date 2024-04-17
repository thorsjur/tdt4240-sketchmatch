package com.groupfive.sketchmatch.serialization

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object SnapshotListSerializer : KSerializer<SnapshotStateList<Offset>> {
    private val offsetSerializer = OffsetSerializer

    override val descriptor = ListSerializer(OffsetSerializer).descriptor

    override fun serialize(encoder: Encoder, value: SnapshotStateList<Offset>) {
        encoder.encodeSerializableValue(ListSerializer(offsetSerializer), value as List<Offset>)
    }

    override fun deserialize(decoder: Decoder): SnapshotStateList<Offset> {
        val list = mutableStateListOf<Offset>()
        val items = decoder.decodeSerializableValue(ListSerializer(offsetSerializer))
        list.addAll(items)
        return list
    }
}