package net.smileycorp.atlas.api.network;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;

public class SimpleStringMessage implements IPacket {

		public SimpleStringMessage() {}

		private String text;

		public SimpleStringMessage(String text) {
			this.text=text;
		}

		public String getText() {
			return text;
		}

		@Override
		public void readPacketData(PacketBuffer buf) throws IOException {
			text = buf.readString();
		}

		@Override
		public void writePacketData(PacketBuffer buf) throws IOException {
			buf.writeString(text);
		}

		@Override
		public void processPacket(INetHandler handler) {}


}
