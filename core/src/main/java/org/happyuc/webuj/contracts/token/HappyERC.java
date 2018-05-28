package org.happyuc.webuj.contracts.token;

import org.happyuc.webuj.abi.EventEncoder;
import org.happyuc.webuj.abi.FunctionEncoder;
import org.happyuc.webuj.abi.TypeReference;
import org.happyuc.webuj.abi.datatypes.Address;
import org.happyuc.webuj.abi.datatypes.DynamicArray;
import org.happyuc.webuj.abi.datatypes.DynamicBytes;
import org.happyuc.webuj.abi.datatypes.Event;
import org.happyuc.webuj.abi.datatypes.Function;
import org.happyuc.webuj.abi.datatypes.Type;
import org.happyuc.webuj.abi.datatypes.Utf8String;
import org.happyuc.webuj.abi.datatypes.generated.Uint256;
import org.happyuc.webuj.abi.datatypes.generated.Uint8;
import org.happyuc.webuj.crypto.Credentials;
import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.core.DefaultBlockParameter;
import org.happyuc.webuj.protocol.core.RemoteCall;
import org.happyuc.webuj.protocol.core.methods.request.HucReqFilter;
import org.happyuc.webuj.protocol.core.methods.response.HucSendRepTransaction;
import org.happyuc.webuj.protocol.core.methods.response.RepTransactionReceipt;
import org.happyuc.webuj.tx.Contract;
import org.happyuc.webuj.tx.FastRawTransactionManager;
import org.happyuc.webuj.tx.TransactionData;
import org.happyuc.webuj.tx.TransactionManager;
import org.happyuc.webuj.utils.Convert;
import rx.Observable;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HappyERC extends Contract implements ERC20Interface {

    public static final String BINARY = "60806040526012600360006101000a81548160ff021916908360ff1602179055503480156200002d57600080fd5b50604051620013a2380380620013a2833981018060405281019080805190602001909291908051820192919060200180518201929190505050600360009054906101000a900460ff1660ff16600a0a8302600281905550600254600460003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508160009080519060200190620000e292919062000105565b508060019080519060200190620000fb92919062000105565b50505050620001b4565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200014857805160ff191683800117855562000179565b8280016001018555821562000179579182015b82811115620001785782518255916020019190600101906200015b565b5b5090506200018891906200018c565b5090565b620001b191905b80821115620001ad57600081600090555060010162000193565b5090565b90565b6111de80620001c46000396000f3006080604052600436106100c5576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306fdde03146100ca578063095ea7b31461015a57806318160ddd146101bf57806323b872dd146101ea578063313ce5671461026f57806342966c68146102a057806351e6fb5e146102e557806370a082311461038e57806379cc6790146103e557806395d89b411461044a578063a9059cbb146104da578063cae9ca5114610527578063dd62ed3e146105d2575b600080fd5b3480156100d657600080fd5b506100df610649565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561011f578082015181840152602081019050610104565b50505050905090810190601f16801561014c5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561016657600080fd5b506101a5600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291905050506106e7565b604051808215151515815260200191505060405180910390f35b3480156101cb57600080fd5b506101d4610774565b6040518082815260200191505060405180910390f35b3480156101f657600080fd5b50610255600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035906020019092919050505061077a565b604051808215151515815260200191505060405180910390f35b34801561027b57600080fd5b506102846108a7565b604051808260ff1660ff16815260200191505060405180910390f35b3480156102ac57600080fd5b506102cb600480360381019080803590602001909291905050506108ba565b604051808215151515815260200191505060405180910390f35b3480156102f157600080fd5b5061038c60048036038101908080359060200190820180359060200190808060200260200160405190810160405280939291908181526020018383602002808284378201915050505050509192919290803590602001908201803590602001908080602002602001604051908101604052809392919081815260200183836020028082843782019150505050505091929192905050506109be565b005b34801561039a57600080fd5b506103cf600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610a15565b6040518082815260200191505060405180910390f35b3480156103f157600080fd5b50610430600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610a2d565b604051808215151515815260200191505060405180910390f35b34801561045657600080fd5b5061045f610c47565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561049f578082015181840152602081019050610484565b50505050905090810190601f1680156104cc5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156104e657600080fd5b50610525600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610ce5565b005b34801561053357600080fd5b506105b8600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050610cf4565b604051808215151515815260200191505060405180910390f35b3480156105de57600080fd5b50610633600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610e77565b6040518082815260200191505060405180910390f35b60008054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156106df5780601f106106b4576101008083540402835291602001916106df565b820191906000526020600020905b8154815290600101906020018083116106c257829003601f168201915b505050505081565b600081600560003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055506001905092915050565b60025481565b6000600560008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054821115151561080757600080fd5b81600560008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000828254039250508190555061089c848484610e9c565b600190509392505050565b600360009054906101000a900460ff1681565b600081600460003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020541015151561090a57600080fd5b81600460003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008282540392505081905550816002600082825403925050819055503373ffffffffffffffffffffffffffffffffffffffff167fcc16f5dbb4873280815c1ee09dbd06736cffcc184412cf7a71a0fdb75d397ca5836040518082815260200191505060405180910390a260019050919050565b60008090505b8251811015610a1057610a0583828151811015156109de57fe5b9060200190602002015183838151811015156109f657fe5b90602001906020020151610ce5565b6001810190506109c4565b505050565b60046020528060005260406000206000915090505481565b600081600460008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205410151515610a7d57600080fd5b600560008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020548211151515610b0857600080fd5b81600460008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000828254039250508190555081600560008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008282540392505081905550816002600082825403925050819055508273ffffffffffffffffffffffffffffffffffffffff167fcc16f5dbb4873280815c1ee09dbd06736cffcc184412cf7a71a0fdb75d397ca5836040518082815260200191505060405180910390a26001905092915050565b60018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610cdd5780601f10610cb257610100808354040283529160200191610cdd565b820191906000526020600020905b815481529060010190602001808311610cc057829003601f168201915b505050505081565b610cf0338383610e9c565b5050565b600080849050610d0485856106e7565b15610e6e578073ffffffffffffffffffffffffffffffffffffffff16638f4ffcb1338630876040518563ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018481526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200180602001828103825283818151815260200191508051906020019080838360005b83811015610dfe578082015181840152602081019050610de3565b50505050905090810190601f168015610e2b5780820380516001836020036101000a031916815260200191505b5095505050505050600060405180830381600087803b158015610e4d57600080fd5b505af1158015610e61573d6000803e3d6000fd5b5050505060019150610e6f565b5b509392505050565b6005602052816000526040600020602052806000526040600020600091509150505481565b6000808373ffffffffffffffffffffffffffffffffffffffff1614151515610ec357600080fd5b81600460008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205410151515610f1157600080fd5b600460008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205482600460008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205401111515610f9f57600080fd5b600460008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054600460008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205401905081600460008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000828254039250508190555081600460008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600082825401925050819055508273ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef846040518082815260200191505060405180910390a380600460008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054600460008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054011415156111ac57fe5b505050505600a165627a7a72305820226ed4fb2dd6da0b47dfa689f45e384bf5afac9503d20f8f6ba5057076a3dec60029";

    public static final List<TypeReference<?>> EMPTY_REFERENCES = Arrays.asList(
            new TypeReference<Address>() {},
            new TypeReference<Address>() {});

    public static final Event TRANSFER_EVENT = new Event(
            "Transfer",
            EMPTY_REFERENCES,
            Collections.singletonList(new TypeReference<Uint256>() {
            }));

    public static final Event APPROVAL_EVENT = new Event(
            "Approval",
            EMPTY_REFERENCES,
            Collections.singletonList(new TypeReference<Uint256>() {
            }));

    public HappyERC(String contractAddress, Webuj webuj, TransactionManager transactionManager, BigInteger price, BigInteger limit) {
        super(BINARY, contractAddress, webuj, transactionManager, price, limit);
    }


    /**
     * Obtain the balance of whos _owner in which Token
     *
     * @param _owner an Account or Wallet address
     * @return BigInteger
     */
    @Override
    public RemoteCall<BigInteger> balanceOf(String _owner) {
        final List<Type> inputParam = Collections.singletonList(new Address(_owner));
        final List<TypeReference<?>> outputParam = Collections.singletonList(new TypeReference<Uint256>() {});
        final Function function = new Function("balanceOf", inputParam, outputParam);
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    /**
     * Transfer balance
     *
     * @param _to    the recipient _to rep whos you want to transfer
     * @param _value transfer balance value
     * @return RepTransactionReceipt
     */
    @Override
    public RemoteCall<RepTransactionReceipt> transfer(String _to, String _value, String _remark) {
        BigInteger weiValue = Convert.toWei(_value, Convert.Unit.HUC).toBigInteger();
        final Function function = new Function("transfer", Arrays.asList(new Address(_to), new Uint256(weiValue)), Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public HucSendRepTransaction transfer2(String _to, String _value, String _remark) throws IOException {
        BigInteger weiValue = Convert.toWei(_value, Convert.Unit.HUC).toBigInteger();
        final Function function = new Function("transfer", Arrays.asList(new Address(_to), new Uint256(weiValue)), Collections.emptyList());
        TransactionData txData = new TransactionData(
                contractAddress,
                FunctionEncoder.encode(function),
                BigInteger.ZERO,
                gasPrice,
                gasLimit);
        return transactionManager.makeReqTransaction(txData).send();
    }

    @Override
    public RemoteCall<RepTransactionReceipt> mulTransfer(List<String> addresses, List<String> values, List<String> remarks) {
        List<BigInteger> weiValues = values
                .stream()
                .map(_value -> Convert.toWei(_value, Convert.Unit.HUC).toBigInteger())
                .collect(Collectors.toList());
        DynamicArray<Address> dests = new DynamicArray<>(addresses.stream().map(Address::new).collect(Collectors.toList()));
        DynamicArray<Uint256> amounts = new DynamicArray<>(weiValues.stream().map(Uint256::new).collect(Collectors.toList()));

        final Function function = new Function("mulTransfer", Arrays.asList(dests, amounts), Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    /**
     * todo ???
     *
     * @param repTransactionReceipt the transaction result, like method `transfer`
     * @return List EventResponse.TransferEr
     */
    public List<EventResponse.TransferEr> simpleGetTxEvents(RepTransactionReceipt repTransactionReceipt) {
        return getTransferEvents(repTransactionReceipt, eventValues -> {
            EventResponse.TransferEr tEr = new EventResponse.TransferEr();
            tEr.log = eventValues.getLog();
            tEr._from = (String) eventValues.getIndexedValues().get(0).getValue();
            tEr._to = (String) eventValues.getIndexedValues().get(1).getValue();
            tEr._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            return tEr;
        });
    }

    @Override
    public <T> List<T> getTransferEvents(RepTransactionReceipt repTransactionReceipt, EventResponse.Rec<T> rec) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFER_EVENT, repTransactionReceipt);
        ArrayList<T> responses = new ArrayList<>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            T t = rec.doLog(eventValues);
            responses.add(t);
        }
        return responses;
    }

    /**
     * todo ???
     *
     * @param startBlock todo ???
     * @param endBlock   todo ???
     * @return Observable EventResponse.TransferEr
     */
    public Observable<EventResponse.TransferEr> simpleTsEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        return transferEventObservable(startBlock, endBlock, eventValues -> {
            EventResponse.TransferEr tEr = new EventResponse.TransferEr();
            tEr.log = eventValues.getLog();
            tEr._from = (String) eventValues.getIndexedValues().get(0).getValue();
            tEr._to = (String) eventValues.getIndexedValues().get(1).getValue();
            tEr._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            return tEr;
        });
    }

    @Override
    public <T> Observable<T> transferEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock, EventResponse.Rec<T> rec) {
        HucReqFilter filter = new HucReqFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
        return transferEventObservable(filter, rec);
    }

    private <T> Observable<T> transferEventObservable(HucReqFilter filter, EventResponse.Rec<T> rec) {
        return webuj.hucLogObservable(filter).map(log -> {
            EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSFER_EVENT, log);
            return rec.doLog(eventValues);
        });
    }

    @Override
    public RemoteCall<BigInteger> allowance(String _owner, String _spender) {
        final List<Type> inputParam = Arrays.asList(new Address(_owner), new Address(_spender));
        final List<TypeReference<?>> outputParam = Collections.singletonList(new TypeReference<Uint256>() {
        });
        final Function function = new Function("allowance", inputParam, outputParam);
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    @Override
    public RemoteCall<RepTransactionReceipt> approve(String _spender, BigInteger _value) {
        final List<Type> inputParam = Arrays.asList(new Address(_spender), new Uint256(_value));
        final List<TypeReference<?>> outputParam = Collections.emptyList();
        final Function function = new Function("approve", inputParam, outputParam);
        return executeRemoteCallTransaction(function);
    }

    @Override
    public RemoteCall<RepTransactionReceipt> transferFrom(String _from, String _to, String _value, String remark) {
        BigInteger weiValue = Convert.toWei(_value, Convert.Unit.HUC).toBigInteger();
        final List<Type> inputParam = Arrays.asList(new Address(_from), new Address(_to), new Uint256(weiValue));
        final List<TypeReference<?>> outputParam = Collections.emptyList();
        final Function function = new Function("transferFrom", inputParam, outputParam);
        return executeRemoteCallTransaction(function);
    }

    /**
     * todo ???
     *
     * @param repTransactionReceipt the transaction result, like method `transfer`
     * @return List EventResponse.ApprovalEr
     */
    public List<EventResponse.ApprovalEr> simpleGetapprEvents(RepTransactionReceipt repTransactionReceipt) {
        return getTransferEvents(repTransactionReceipt, eventValues -> {
            EventResponse.ApprovalEr aEr = new EventResponse.ApprovalEr();
            aEr.log = eventValues.getLog();
            aEr._owner = (String) eventValues.getIndexedValues().get(0).getValue();
            aEr._spender = (String) eventValues.getIndexedValues().get(1).getValue();
            aEr._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            return aEr;
        });
    }

    @Override
    public <T> List<T> getApprovalEvents(RepTransactionReceipt repTransactionReceipt, EventResponse.Rec<T> rec) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(APPROVAL_EVENT, repTransactionReceipt);
        ArrayList<T> responses = new ArrayList<>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            T t = rec.doLog(eventValues);
            responses.add(t);
        }
        return responses;
    }

    /**
     * todo ???
     *
     * @param startBlock todo ???
     * @param endBlock   todo ???
     * @return Observable EventResponse.ApprovalEr
     */
    public Observable<EventResponse.ApprovalEr> simpleAppEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        return approvalEventObservable(startBlock, endBlock, eventValues -> {
            EventResponse.ApprovalEr aEr = new EventResponse.ApprovalEr();
            aEr.log = eventValues.getLog();
            aEr._owner = (String) eventValues.getIndexedValues().get(0).getValue();
            aEr._spender = (String) eventValues.getIndexedValues().get(1).getValue();
            aEr._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            return aEr;
        });
    }

    @Override
    public <T> Observable<T> approvalEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock, EventResponse.Rec<T> rec) {
        HucReqFilter filter = new HucReqFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVAL_EVENT));
        return approvalEventObservable(filter, rec);
    }

    private <T> Observable<T> approvalEventObservable(HucReqFilter filter, EventResponse.Rec<T> rec) {
        return webuj.hucLogObservable(filter).map(log -> {
            EventValuesWithLog eventValues = extractEventParametersWithLog(APPROVAL_EVENT, log);
            return rec.doLog(eventValues);
        });
    }

    /**
     * Get the token name which Token
     *
     * @return String
     */
    public RemoteCall<String> name() {
        final List<Type> inputParam = Collections.emptyList();
        final List<TypeReference<?>> outputParam = Collections.singletonList(new TypeReference<Utf8String>() {
        });
        final Function function = new Function("name", inputParam, outputParam);
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    /**
     * Get the token symbol which Token
     *
     * @return String
     */
    public RemoteCall<String> symbol() {
        final List<Type> inputParam = Collections.emptyList();
        final List<TypeReference<?>> outputParam = Collections.singletonList(new TypeReference<Utf8String>() {
        });
        final Function function = new Function("symbol", inputParam, outputParam);
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    /**
     * Get the token decimals which Token
     *
     * @return String
     */
    public RemoteCall<BigInteger> decimals() {
        final List<Type> inputParam = Collections.emptyList();
        final List<TypeReference<?>> outputParam = Collections.singletonList(new TypeReference<Uint8>() {
        });
        final Function function = new Function("decimals", inputParam, outputParam);
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    /**
     * Get the total circulation which Token
     *
     * @return BigInteger
     */
    @Override
    public RemoteCall<BigInteger> totalSupply() {
        final List<Type> inputParam = Collections.emptyList();
        final List<TypeReference<?>> outputParam = Collections.singletonList(new TypeReference<Uint256>() {
        });
        final Function function = new Function("totalSupply", inputParam, outputParam);
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<RepTransactionReceipt> approveAndCall(String _spender, BigInteger _value, byte[] _extraData) {
        final List<Type> inputParam = Arrays.asList(new Address(_spender), new Uint256(_value), new DynamicBytes(_extraData));
        final List<TypeReference<?>> outputParam = Collections.emptyList();
        final Function function = new Function("approveAndCall", inputParam, outputParam);
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<HappyERC> deploy(Webuj webuj, Credentials credentials, BigInteger price, BigInteger limit, BigInteger amount, String name, String symbol) {
        List<Type> param = Arrays.asList(new Uint256(amount), new Utf8String(name), new Utf8String(symbol));
        String encodedConstructor = FunctionEncoder.encodeConstructor(param);
        return deployRemoteCall(HappyERC.class, webuj, credentials, price, limit, BINARY, encodedConstructor);
    }

    public static RemoteCall<HappyERC> deploy(Webuj webuj, TransactionManager manager, BigInteger price, BigInteger limit, BigInteger amount, String name, String symbol) {
        List<Type> param = Arrays.asList(new Uint256(amount), new Utf8String(name), new Utf8String(symbol));
        String encodedConstructor = FunctionEncoder.encodeConstructor(param);
        return deployRemoteCall(HappyERC.class, webuj, manager, price, limit, BINARY, encodedConstructor);
    }

    public static HappyERC load(String contractAddress, Webuj webuj, Credentials credentials) {
        return load(contractAddress, webuj, new FastRawTransactionManager(webuj, credentials));
    }

    public static HappyERC load(String contractAddress, Webuj webuj, TransactionManager transactionManager) {
        return new HappyERC(contractAddress, webuj, transactionManager, GAS_PRICE, GAS_LIMIT);
    }

}
