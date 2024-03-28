package net.smileycorp.atlas.api.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;


public class GenericByteMessage extends AbstractMessage {

	public GenericByteMessage() {}

	private byte[] data;

	public GenericByteMessage(byte[] data) {
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

	@Override
	public void process(NetworkEvent.Context ctx) {
		throw new IllegalArgumentException("Please use the other register method, when using generic messages!");
	}

}
