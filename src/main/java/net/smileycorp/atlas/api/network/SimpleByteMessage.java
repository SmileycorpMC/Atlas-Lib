package net.smileycorp.atlas.api.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
<<<<<<< Updated upstream
import net.minecraft.network.protocol.Packet;

public class SimpleByteMessage<T extends PacketListener> implements Packet<T> {
=======

public class SimpleByteMessage extends SimpleAbstractMessage {
>>>>>>> Stashed changes

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
<<<<<<< Updated upstream
			buf.writeBytes(data);	
		}

		@Override
		public void handle(T listener) {
			
		}

=======
			buf.writeBytes(data);
		}
		
		@Override
		public void read(FriendlyByteBuf buf) {
			data = new byte[buf.readableBytes()];
			buf.readBytes(data);
		}

		@Override
		public void handle(PacketListener listener) {}

>>>>>>> Stashed changes
}
