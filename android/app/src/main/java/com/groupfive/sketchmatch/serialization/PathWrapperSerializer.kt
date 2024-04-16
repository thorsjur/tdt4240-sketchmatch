package com.groupfive.sketchmatch.serialization

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import io.ak1.drawbox.PathWrapper
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

object PathWrapperSerializer : KSerializer<PathWrapper> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("PathWrapper") {
        element("points", SnapshotListSerializer.descriptor)
        element("strokeWidth", PrimitiveSerialDescriptor("strokeWidth", PrimitiveKind.FLOAT))
        element("strokeColor", ColorSerializer.descriptor)
        element("alpha", PrimitiveSerialDescriptor("alpha", PrimitiveKind.FLOAT))
    }

    override fun serialize(encoder: Encoder, value: PathWrapper) {
        encoder.encodeStructure(descriptor) {
            encodeSerializableElement(descriptor, 0, SnapshotListSerializer, value.points)
            encodeFloatElement(descriptor, 1, value.strokeWidth)
            encodeSerializableElement(descriptor, 2, ColorSerializer, value.strokeColor)
            encodeFloatElement(descriptor, 3, value.alpha)
        }
    }

    override fun deserialize(decoder: Decoder): PathWrapper = decoder.decodeStructure(
        descriptor
    ) {
        var points: SnapshotStateList<Offset> = SnapshotStateList()
        var strokeWidth: Float? = null
        var strokeColor: Color? = null
        var alpha = 1.0f

        while (true) {
            when (val index = decodeElementIndex(descriptor)) {
                0 -> points = decodeSerializableElement(descriptor, 0, SnapshotListSerializer)
                1 -> strokeWidth = decodeFloatElement(descriptor, 1)
                2 -> strokeColor = decodeSerializableElement(descriptor, 2, ColorSerializer)
                3 -> alpha = decodeFloatElement(descriptor, 3)
                CompositeDecoder.DECODE_DONE -> break
                else -> error("Unexpected index: $index")
            }
        }

        PathWrapper(points, requireNotNull(strokeWidth), requireNotNull(strokeColor), alpha)
    }
}