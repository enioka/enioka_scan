package com.enioka.scanner.sdk.zebraoss.commands;

import com.enioka.scanner.bt.api.Command;
import com.enioka.scanner.bt.api.Scanner;
import com.enioka.scanner.sdk.zebraoss.ssi.SsiMonoPacket;
import com.enioka.scanner.sdk.zebraoss.ssi.SsiStatus;

public abstract class CommandExpectingNothing implements Command<Void> {
    private final SsiMonoPacket packet;

    public CommandExpectingNothing(byte opCode, byte[] data) {
        packet = new SsiMonoPacket(opCode, SsiStatus.DEFAULT.getByte(), data);
    }

    public CommandExpectingNothing(byte opCode) {
        packet = new SsiMonoPacket(opCode, SsiStatus.DEFAULT.getByte(), new byte[0]);
    }

    @Override
    public byte[] getCommand() {
        return packet.toCommandBuffer(false);
    }

    @Override
    public byte[] getCommand(final Scanner scanner) {
        return packet.toCommandBuffer(scanner.isBleDevice());
    }

    @Override
    public Class<? extends Void> getReturnType() {
        return null;
    }

    @Override
    public int getTimeOut() {
        return 1000;
    }
}
