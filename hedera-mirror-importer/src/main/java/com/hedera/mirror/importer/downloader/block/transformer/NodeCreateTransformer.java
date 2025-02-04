package com.hedera.mirror.importer.downloader.block.transformer;

import com.hedera.hapi.block.stream.output.protoc.StateIdentifier;
import com.hedera.mirror.common.domain.transaction.BlockItem;
import com.hedera.mirror.common.domain.transaction.TransactionType;
import com.hederahashgraph.api.proto.java.TransactionRecord;


public class NodeCreateTransformer extends AbstractBlockItemTransformer {

    @Override
    protected void updateTransactionRecord(BlockItem blockItem, TransactionRecord.Builder transactionRecordBuilder) {

        for (var stateChange : blockItem.stateChanges()) {
            for (var change : stateChange.getStateChangesList()) {
                if (change.getStateId() == StateIdentifier.STATE_ID_NODES.getNumber() && change.hasMapUpdate()) {
                    var key = change.getMapUpdate().getKey();
                    if (key.hasEntityNumberKey()) {
                        transactionRecordBuilder.getReceiptBuilder().setNodeId(key.getEntityNumberKey().getValue());
                        return;
                    }
                }
            }
        }
    }

    @Override
    public TransactionType getType() {
        return TransactionType.NODECREATE;
    }
}
