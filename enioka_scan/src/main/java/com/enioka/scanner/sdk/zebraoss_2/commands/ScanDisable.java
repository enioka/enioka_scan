package com.enioka.scanner.sdk.zebraoss_2.commands;

import com.enioka.scanner.sdk.zebraoss_2.ssi.SsiCommand;

public class ScanDisable extends CommandExpectingAck {
    public ScanDisable(boolean isBle) {
        super(SsiCommand.SCAN_DISABLE.getOpCode(), isBle);
    }
}
