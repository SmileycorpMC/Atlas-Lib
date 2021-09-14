package net.smileycorp.atlas.api.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;


public class SimpleByteMessage extends SimpleAbstractMessage {

		public SimpleByteMessage() {}

		private byte[] data;

		public SimpleByteMessage(byte[] data) {
			this.data=data;
		}

		public byte[] getData() {
			return data;
		}

		@Override
		public void write(FriendlyByteBuf buf) {
			buf.writeBytes(data);	
		}
		
		@Override
		public void read(FriendlyByteBuf buf) {
			data = new byte[buf.readableBytes()];
			buf.readBytes(data);
		}

		@Override
		public void handle(PacketListener listener) {}

}
