package org.bumble.manager.test;

import org.bumble.base.test.BumbleTest;
import org.bumble.manager.txn.Txn;
import org.bumble.manager.txn.TxnGroup;

public class TxnGroupTest extends BumbleTest {
	public static void main( String[] args ) throws Exception
    {
		TxnGroup txnGroup = new TxnGroup("nnn");
		txnGroup.addUpdateTxn(new Txn("sss1", "sss1-xxx", "ttt1"));
		txnGroup.addUpdateTxn(new Txn("sss2", "sss2-yyy", "ttt2"));
		
		String str = txnGroup.toJsonString();
		logger.info(str);
		
		TxnGroup txnGroup2 = TxnGroup.parseJson(str);
		logger.info(txnGroup2.toJsonString());
    }

	@Override
	public void template(String[] args) throws Exception {
		
	}
}
