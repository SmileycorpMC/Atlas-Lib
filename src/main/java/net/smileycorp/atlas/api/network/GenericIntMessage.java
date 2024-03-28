package net.smileycorp.atlas.api.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;


public class GenericIntMessage extends AbstractMessage {

	public GenericIntMessage() {}

	private int value;

	public GenericIntMessage(int value) {
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
	public void read(FriendlyByteBuf buf) {
		value = buf.readInt();
	}

	@Override
	public void handle(PacketListener listener) {}

	@Override
	public void process(NetworkEvent.Context ctx) {
		throw new IllegalArgumentException("Please use the other register method, when using generic messages!");
	}

}
