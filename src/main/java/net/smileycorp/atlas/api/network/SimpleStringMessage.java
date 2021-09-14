package net.smileycorp.atlas.api.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
<<<<<<< Updated upstream
import net.minecraft.network.protocol.Packet;

public class SimpleStringMessage<T extends PacketListener> implements Packet<T> {
=======

public class SimpleStringMessage extends SimpleAbstractMessage {
>>>>>>> Stashed changes

		public SimpleStringMessage() {}

		private String text;

		public SimpleStringMessage(String text) {
			this.text=text;
		}

		public String getText() {
			return text;
		}

		@Override
		public void write(FriendlyByteBuf buf) {
			buf.writeUtf(text);
		}

		@Override
<<<<<<< Updated upstream
		public void handle(T listener) {
			
		}


=======
		public void read(FriendlyByteBuf buf) {
			text = buf.readUtf();
		}

		@Override
		public void handle(PacketListener listener) {}
		
>>>>>>> Stashed changes
}
