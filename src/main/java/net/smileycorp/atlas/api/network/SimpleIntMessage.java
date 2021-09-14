package net.smileycorp.atlas.api.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
<<<<<<< Updated upstream
import net.minecraft.network.protocol.Packet;

public class SimpleIntMessage<T extends PacketListener> implements Packet<T> {
=======

public class SimpleIntMessage extends SimpleAbstractMessage {
>>>>>>> Stashed changes

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
<<<<<<< Updated upstream

		@Override
		public void handle(T listener) {
			
		}

=======
		
		@Override
		public void read(FriendlyByteBuf buf) {
			value = buf.readInt();
		}

		@Override
		public void handle(PacketListener listener) {}
>>>>>>> Stashed changes

}
