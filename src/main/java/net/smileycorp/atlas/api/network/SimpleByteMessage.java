package net.smileycorp.atlas.api.network;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;

public class SimpleByteMessage implements IPacket<INetHandler> {

		public SimpleByteMessage() {}

		private byte[] data;

		public SimpleByteMessage(byte[] data) {
			this.data=data;
		}

		public byte[] getData() {
			return data;
		}

		@Override
		public void read(PacketBuffer buf) throws IOException {
			data = new byte[buf.readableBytes()];
			buf.readBytes(data);
		}

		@Override
		public void write(PacketBuffer buf) throws IOException {
			buf.writeBytes(data);
		}

		@Override
		public void handle(INetHandler handler) {}

}
