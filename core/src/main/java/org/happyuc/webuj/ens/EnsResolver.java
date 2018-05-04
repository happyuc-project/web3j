package org.happyuc.webuj.ens;

import org.happyuc.webuj.crypto.WalletUtils;
import org.happyuc.webuj.ens.contracts.generated.ENS;
import org.happyuc.webuj.ens.contracts.generated.PublicResolver;
import org.happyuc.webuj.protocol.webuj;
import org.happyuc.webuj.protocol.core.DefaultBlockParameterName;
import org.happyuc.webuj.protocol.core.methods.response.HucBlock;
import org.happyuc.webuj.protocol.core.methods.response.HucSyncing;
import org.happyuc.webuj.protocol.core.methods.response.NetVersion;
import org.happyuc.webuj.tx.ClientTransactionManager;
import org.happyuc.webuj.tx.ManagedTransaction;
import org.happyuc.webuj.tx.TransactionManager;
import org.happyuc.webuj.utils.Numeric;

/**
 * Resolution logic for contract addresses.
 */
public class EnsResolver {

    static final long DEFAULT_SYNC_THRESHOLD = 1000 * 60 * 3;
    static final String REVERSE_NAME_SUFFIX = ".addr.reverse";

    private final webuj webuj;
    private final TransactionManager transactionManager;
    private long syncThreshold;  // non-final in case this value needs to be tweaked

    public EnsResolver(webuj webuj, long syncThreshold) {
        this.webuj = webuj;
        transactionManager = new ClientTransactionManager(webuj, null);  // don't use empty string
        this.syncThreshold = syncThreshold;
    }

    public EnsResolver(webuj webuj) {
        this(webuj, DEFAULT_SYNC_THRESHOLD);
    }

    public void setSyncThreshold(long syncThreshold) {
        this.syncThreshold = syncThreshold;
    }

    public long getSyncThreshold() {
        return syncThreshold;
    }

    /**
     * Provides an access to a valid public resolver in order to access other API methods.
     * @param ensName our user input ENS name
     * @return PublicResolver
     */
    public PublicResolver obtainPublicResolver(String ensName) {
        if (isValidEnsName(ensName)) {
            try {
                if (!isSynced()) {
                    throw new EnsResolutionException("Node is not currently synced");
                } else {
                    return lookupResolver(ensName);
                }
            } catch (Exception e) {
                throw new EnsResolutionException("Unable to determine sync status of node", e);
            }

        } else {
            throw new EnsResolutionException("EnsName is invalid: " + ensName);
        }
    }

    public String resolve(String contractId) {
        if (isValidEnsName(contractId)) {
            PublicResolver resolver = obtainPublicResolver(contractId);

            byte[] nameHash = NameHash.nameHashAsBytes(contractId);
            String contractAddress = null;
            try {
                contractAddress = resolver.addr(nameHash).send();
            } catch (Exception e) {
                throw new RuntimeException("Unable to execute HappyUC request", e);
            }

            if (!WalletUtils.isValidAddress(contractAddress)) {
                throw new RuntimeException("Unable to resolve address for name: " + contractId);
            } else {
                return contractAddress;
            }
        } else {
            return contractId;
        }
    }

    /**
     * Reverse name resolution as documented in the
     * <a href="https://docs.ens.domains/en/latest/userguide.html#reverse-name-resolution">specification</a>.
     * @param address an happyuc address, example: "0x314159265dd8dbb310642f98f50c066173c1259b"
     * @return a EnsName registered for provided address
     */
    public String reverseResolve(String address) {
        if (WalletUtils.isValidAddress(address)) {
            String reverseName = Numeric.cleanHexPrefix(address) + REVERSE_NAME_SUFFIX;
            PublicResolver resolver = obtainPublicResolver(reverseName);

            byte[] nameHash = NameHash.nameHashAsBytes(reverseName);
            String name = null;
            try {
                name = resolver.name(nameHash).send();
            } catch (Exception e) {
                throw new RuntimeException("Unable to execute HappyUC request", e);
            }

            if (!isValidEnsName(name)) {
                throw new RuntimeException("Unable to resolve name for address: " + address);
            } else {
                return name;
            }
        } else {
            throw new EnsResolutionException("Address is invalid: " + address);
        }
    }

    PublicResolver lookupResolver(String ensName) throws Exception {
        NetVersion netVersion = webuj.netVersion().send();
        String registryContract = Contracts.resolveRegistryContract(netVersion.getNetVersion());

        ENS ensRegistry = ENS.load(
                registryContract, webuj, transactionManager,
                ManagedTransaction.GAS_PRICE, org.happyuc.webuj.tx.Contract.GAS_LIMIT);

        byte[] nameHash = NameHash.nameHashAsBytes(ensName);

        String resolverAddress = ensRegistry.resolver(nameHash).send();
        PublicResolver resolver = PublicResolver.load(
                resolverAddress, webuj, transactionManager,
                ManagedTransaction.GAS_PRICE, org.happyuc.webuj.tx.Contract.GAS_LIMIT);

        return resolver;
    }

    boolean isSynced() throws Exception {
        HucSyncing hucSyncing = webuj.hucSyncing().send();
        if (ethSyncing.isSyncing()) {
            return false;
        } else {
            HucBlock hucBlock =
                    webuj.hucGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send();
            long timestamp = hucBlock.getBlock().getTimestamp().longValueExact() * 1000;

            return System.currentTimeMillis() - syncThreshold < timestamp;
        }
    }

    public static boolean isValidEnsName(String input) {
        return input != null  // will be set to null on new Contract creation
                && (input.contains(".") || !WalletUtils.isValidAddress(input));
    }
}
