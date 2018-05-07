package org.happyuc.webuj.ens;

import java.io.IOException;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import org.happyuc.webuj.abi.TypeEncoder;
import org.happyuc.webuj.abi.datatypes.Utf8String;
import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.WebujService;
import org.happyuc.webuj.protocol.core.Request;
import org.happyuc.webuj.protocol.core.methods.response.HucBlock;
import org.happyuc.webuj.protocol.core.methods.response.HucCall;
import org.happyuc.webuj.protocol.core.methods.response.HucSyncing;
import org.happyuc.webuj.protocol.core.methods.response.NetVersion;
import org.happyuc.webuj.tx.ChainId;
import org.happyuc.webuj.utils.Numeric;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.happyuc.webuj.ens.EnsResolver.DEFAULT_SYNC_THRESHOLD;
import static org.happyuc.webuj.ens.EnsResolver.isValidEnsName;

public class EnsResolverTest {

    private WebujService webujService;
    private EnsResolver ensResolver;

    @Before
    public void setUp() {
        webujService = mock(WebujService.class);
        Webuj webuj = Webuj.build(webujService);
        ensResolver = new EnsResolver(webuj);
    }

    @Test
    public void testResolve() throws Exception {
        configureSyncing(false);
        configureLatestBlock(System.currentTimeMillis() / 1000);  // block timestamp is in seconds

        NetVersion netVersion = new NetVersion();
        netVersion.setResult(Byte.toString(ChainId.MAINNET));

        String resolverAddress = "0x0000000000000000000000004c641fb9bad9b60ef180c31f56051ce826d21a9a";
        String contractAddress = "0x00000000000000000000000019e03255f667bdfd50a32722df860b1eeaf4d635";

        HucCall resolverAddressResponse = new HucCall();
        resolverAddressResponse.setResult(resolverAddress);

        HucCall contractAddressResponse = new HucCall();
        contractAddressResponse.setResult(contractAddress);

        when(webujService.send(any(Request.class), eq(NetVersion.class))).thenReturn(netVersion);
        when(webujService.send(any(Request.class), eq(HucCall.class))).thenReturn(resolverAddressResponse);
        when(webujService.send(any(Request.class), eq(HucCall.class))).thenReturn(contractAddressResponse);

        assertThat(ensResolver.resolve("webuj.huc"), is("0x19e03255f667bdfd50a32722df860b1eeaf4d635"));
    }

    @Test
    public void testReverseResolve() throws Exception {
        configureSyncing(false);
        configureLatestBlock(System.currentTimeMillis() / 1000);  // block timestamp is in seconds

        NetVersion netVersion = new NetVersion();
        netVersion.setResult(Byte.toString(ChainId.MAINNET));

        String resolverAddress = "0x0000000000000000000000004c641fb9bad9b60ef180c31f56051ce826d21a9a";
        String contractName = "0x0000000000000000000000000000000000000000000000000000000000000020" + TypeEncoder.encode(new Utf8String("webuj.huc"));
        System.err.println(contractName);

        HucCall resolverAddressResponse = new HucCall();
        resolverAddressResponse.setResult(resolverAddress);

        HucCall contractNameResponse = new HucCall();
        contractNameResponse.setResult(contractName);

        when(webujService.send(any(Request.class), eq(NetVersion.class))).thenReturn(netVersion);
        when(webujService.send(any(Request.class), eq(HucCall.class))).thenReturn(resolverAddressResponse);
        when(webujService.send(any(Request.class), eq(HucCall.class))).thenReturn(contractNameResponse);

        assertThat(ensResolver.reverseResolve("0x19e03255f667bdfd50a32722df860b1eeaf4d635"), is("webuj.huc"));
    }

    @Test
    public void testIsSyncedSyncing() throws Exception {
        configureSyncing(true);

        assertFalse(ensResolver.isSynced());
    }

    @Test
    public void testIsSyncedFullySynced() throws Exception {
        configureSyncing(false);
        configureLatestBlock(System.currentTimeMillis() / 1000);  // block timestamp is in seconds

        assertTrue(ensResolver.isSynced());
    }

    @Test
    public void testIsSyncedBelowThreshold() throws Exception {
        configureSyncing(false);
        configureLatestBlock((System.currentTimeMillis() / 1000) - DEFAULT_SYNC_THRESHOLD);

        assertFalse(ensResolver.isSynced());
    }

    private void configureSyncing(boolean isSyncing) throws IOException {
        HucSyncing hucSyncing = new HucSyncing();
        HucSyncing.Result result = new HucSyncing.Result();
        result.setSyncing(isSyncing);
        hucSyncing.setResult(result);

        when(webujService.send(any(Request.class), eq(HucSyncing.class))).thenReturn(hucSyncing);
    }

    private void configureLatestBlock(long timestamp) throws IOException {
        HucBlock.Block block = new HucBlock.Block();
        block.setTimestamp(Numeric.encodeQuantity(BigInteger.valueOf(timestamp)));
        HucBlock hucBlock = new HucBlock();
        hucBlock.setResult(block);

        when(webujService.send(any(Request.class), eq(HucBlock.class))).thenReturn(hucBlock);
    }

    @Test
    public void testIsEnsName() {
        assertTrue(isValidEnsName("huc"));
        assertTrue(isValidEnsName("web3.huc"));
        assertTrue(isValidEnsName("0x19e03255f667bdfd50a32722df860b1eeaf4d635.huc"));

        assertFalse(isValidEnsName("0x19e03255f667bdfd50a32722df860b1eeaf4d635"));
        assertFalse(isValidEnsName("19e03255f667bdfd50a32722df860b1eeaf4d635"));

        assertTrue(isValidEnsName(""));
    }
}
