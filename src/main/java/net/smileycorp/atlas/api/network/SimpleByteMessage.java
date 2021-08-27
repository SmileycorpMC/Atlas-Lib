package net.smileycorp.atlas.api.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;

public class SimpleByteMessage<T extends PacketListener> implements Packet<T> {

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
		public void handle(T listener) {
			
		}

}
