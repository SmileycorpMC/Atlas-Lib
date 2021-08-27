package net.smileycorp.atlas.api.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;

public class SimpleIntMessage<T extends PacketListener> implements Packet<T> {

		public SimpleIntMessage() {}

		private int value;

		public SimpleIntMessage(int value) {
			this.value=value;
		}

		public int get() {
			return value;
		}

		@Override
		public void write(FriendlyByteBuf buf) {
			buf.writeInt(value);
		}

		@Override
		public void handle(T listener) {
			
		}


}
