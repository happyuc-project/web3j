package org.happyuc.webuj.ens;

import org.junit.Test;

import org.happyuc.webuj.tx.ChainId;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.happyuc.webuj.ens.Contracts.MAINNET;
import static org.happyuc.webuj.ens.Contracts.RINKEBY;
import static org.happyuc.webuj.ens.Contracts.ROPSTEN;
import static org.happyuc.webuj.ens.Contracts.resolveRegistryContract;

public class ContractsTest {

    @Test
    public void testResolveRegistryContract() {
        assertThat(resolveRegistryContract(ChainId.MAINNET + ""), is(MAINNET));
        assertThat(resolveRegistryContract(ChainId.ROPSTEN + ""), is(ROPSTEN));
        assertThat(resolveRegistryContract(ChainId.RINKEBY + ""), is(RINKEBY));
    }

    @Test(expected = EnsResolutionException.class)
    public void testResolveRegistryContractInvalid() {
        resolveRegistryContract(ChainId.NONE + "");
    }
}
