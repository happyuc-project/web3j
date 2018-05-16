package org.happyuc.webuj.contracts.token;

import org.happyuc.webuj.abi.EventEncoder;
import org.happyuc.webuj.abi.FunctionEncoder;
import org.happyuc.webuj.abi.TypeReference;
import org.happyuc.webuj.abi.datatypes.Address;
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
import org.happyuc.webuj.protocol.core.methods.response.RepTransactionReceipt;
import org.happyuc.webuj.tx.Contract;
import org.happyuc.webuj.tx.TransactionManager;
import org.happyuc.webuj.utils.Convert;
import rx.Observable;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ERC20Token extends Contract implements ERC20Interface {
    private static final String BINARY = "60806040526012600360006101000a81548160ff021916908360ff1602179055503480156200002d57600080fd5b50604051620019d7380380620019d7833981018060405281019080805190602001909291908051820192919060200180518201929190505050828282336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600360009054906101000a900460ff1660ff16600a0a8302600481905550600454600560003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508160019080519060200190620001259291906200014b565b5080600290805190602001906200013e9291906200014b565b50505050505050620001fa565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200018e57805160ff1916838001178555620001bf565b82800160010185558215620001bf579182015b82811115620001be578251825591602001919060010190620001a1565b5b509050620001ce9190620001d2565b5090565b620001f791905b80821115620001f3576000816000905550600101620001d9565b5090565b90565b6117cd806200020a6000396000f300608060405260043610610128576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806305fefda71461012d57806306fdde0314610164578063095ea7b3146101f457806318160ddd1461025957806323b872dd14610284578063313ce5671461030957806342966c681461033a5780634b7503341461037f57806370a08231146103aa57806379c650681461040157806379cc67901461044e5780638620410b146104b35780638da5cb5b146104de57806395d89b4114610535578063a6f2ae3a146105c5578063a9059cbb146105cf578063b414d4b61461061c578063cae9ca5114610677578063dd62ed3e14610722578063e4849b3214610799578063e724529c146107c6578063f2fde38b14610815575b600080fd5b34801561013957600080fd5b506101626004803603810190808035906020019092919080359060200190929190505050610858565b005b34801561017057600080fd5b506101796108c5565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156101b957808201518184015260208101905061019e565b50505050905090810190601f1680156101e65780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561020057600080fd5b5061023f600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610963565b604051808215151515815260200191505060405180910390f35b34801561026557600080fd5b5061026e6109f0565b6040518082815260200191505060405180910390f35b34801561029057600080fd5b506102ef600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291905050506109f6565b604051808215151515815260200191505060405180910390f35b34801561031557600080fd5b5061031e610b23565b604051808260ff1660ff16815260200191505060405180910390f35b34801561034657600080fd5b5061036560048036038101908080359060200190929190505050610b36565b604051808215151515815260200191505060405180910390f35b34801561038b57600080fd5b50610394610c3a565b6040518082815260200191505060405180910390f35b3480156103b657600080fd5b506103eb600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610c40565b6040518082815260200191505060405180910390f35b34801561040d57600080fd5b5061044c600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610c58565b005b34801561045a57600080fd5b50610499600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610dc9565b604051808215151515815260200191505060405180910390f35b3480156104bf57600080fd5b506104c8610fe3565b6040518082815260200191505060405180910390f35b3480156104ea57600080fd5b506104f3610fe9565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561054157600080fd5b5061054a61100e565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561058a57808201518184015260208101905061056f565b50505050905090810190601f1680156105b75780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6105cd6110ac565b005b3480156105db57600080fd5b5061061a600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291905050506110cc565b005b34801561062857600080fd5b5061065d600480360381019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506110db565b604051808215151515815260200191505060405180910390f35b34801561068357600080fd5b50610708600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506110fb565b604051808215151515815260200191505060405180910390f35b34801561072e57600080fd5b50610783600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061127e565b6040518082815260200191505060405180910390f35b3480156107a557600080fd5b506107c4600480360381019080803590602001909291905050506112a3565b005b3480156107d257600080fd5b50610813600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803515159060200190929190505050611326565b005b34801561082157600080fd5b50610856600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061144b565b005b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156108b357600080fd5b81600781905550806008819055505050565b60018054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561095b5780601f106109305761010080835404028352916020019161095b565b820191906000526020600020905b81548152906001019060200180831161093e57829003601f168201915b505050505081565b600081600660003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055506001905092915050565b60045481565b6000600660008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020548211151515610a8357600080fd5b81600660008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008282540392505081905550610b188484846114e9565b600190509392505050565b600360009054906101000a900460ff1681565b600081600560003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205410151515610b8657600080fd5b81600560003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008282540392505081905550816004600082825403925050819055503373ffffffffffffffffffffffffffffffffffffffff167fcc16f5dbb4873280815c1ee09dbd06736cffcc184412cf7a71a0fdb75d397ca5836040518082815260200191505060405180910390a260019050919050565b60075481565b60056020528060005260406000206000915090505481565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610cb357600080fd5b80600560008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008282540192505081905550806004600082825401925050819055503073ffffffffffffffffffffffffffffffffffffffff1660007fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef836040518082815260200191505060405180910390a38173ffffffffffffffffffffffffffffffffffffffff163073ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef836040518082815260200191505060405180910390a35050565b600081600560008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205410151515610e1957600080fd5b600660008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020548211151515610ea457600080fd5b81600560008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000828254039250508190555081600660008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008282540392505081905550816004600082825403925050819055508273ffffffffffffffffffffffffffffffffffffffff167fcc16f5dbb4873280815c1ee09dbd06736cffcc184412cf7a71a0fdb75d397ca5836040518082815260200191505060405180910390a26001905092915050565b60085481565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60028054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156110a45780601f10611079576101008083540402835291602001916110a4565b820191906000526020600020905b81548152906001019060200180831161108757829003601f168201915b505050505081565b6000600854348115156110bb57fe5b0490506110c93033836114e9565b50565b6110d73383836114e9565b5050565b60096020528060005260406000206000915054906101000a900460ff1681565b60008084905061110b8585610963565b15611275578073ffffffffffffffffffffffffffffffffffffffff16638f4ffcb1338630876040518563ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018481526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200180602001828103825283818151815260200191508051906020019080838360005b838110156112055780820151818401526020810190506111ea565b50505050905090810190601f1680156112325780820380516001836020036101000a031916815260200191505b5095505050505050600060405180830381600087803b15801561125457600080fd5b505af1158015611268573d6000803e3d6000fd5b5050505060019150611276565b5b509392505050565b6006602052816000526040600020602052806000526040600020600091509150505481565b60075481023073ffffffffffffffffffffffffffffffffffffffff1631101515156112cd57600080fd5b6112d83330836114e9565b3373ffffffffffffffffffffffffffffffffffffffff166108fc60075483029081150290604051600060405180830381858888f19350505050158015611322573d6000803e3d6000fd5b5050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561138157600080fd5b80600960008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055507f48335238b4855f35377ed80f164e8c6f3c366e54ac00b96a6402d4a9814a03a58282604051808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001821515151581526020019250505060405180910390a15050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156114a657600080fd5b806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b60008273ffffffffffffffffffffffffffffffffffffffff161415151561150f57600080fd5b80600560008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020541015151561155d57600080fd5b600560008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205481600560008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054011115156115eb57600080fd5b600960008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff1615151561164457600080fd5b600960008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff1615151561169d57600080fd5b80600560008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000828254039250508190555080600560008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600082825401925050819055508173ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef836040518082815260200191505060405180910390a35050505600a165627a7a72305820163ffd600f7d532a129afaddfb744da83b81b8f0001509d539e8e72bf196f95c0029";

    public static final List<TypeReference<?>> EMPTY_REFERENCES = Arrays.asList(new TypeReference<Address>() {}, new TypeReference<Address>() {});

    public static final Event TRANSFER_EVENT = new Event("Transfer", EMPTY_REFERENCES, Collections.singletonList(new TypeReference<Uint256>() {}));

    public static final Event APPROVAL_EVENT = new Event("Approval", EMPTY_REFERENCES, Collections.singletonList(new TypeReference<Uint256>() {}));

    public ERC20Token(String contractAddress, Webuj webuj, Credentials credentials, BigInteger price, BigInteger limit) {
        super(BINARY, contractAddress, webuj, credentials, price, limit);
    }

    public ERC20Token(String contractAddress, Webuj webuj, TransactionManager transactionManager, BigInteger price, BigInteger limit) {
        super(BINARY, contractAddress, webuj, transactionManager, price, limit);
    }


    /**
     * Obtain the total circulation which Token
     *
     * @return BigInteger
     */
    @Override
    public RemoteCall<BigInteger> totalSupply() {
        final List<Type> inputParam = Collections.emptyList();
        final List<TypeReference<?>> outputParam = Collections.singletonList(new TypeReference<Uint256>() {});
        final Function function = new Function("totalSupply", inputParam, outputParam);
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
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
    public RemoteCall<RepTransactionReceipt> transfer(String _to, BigInteger _value, Convert.Unit unit, String _remark) {
        BigInteger weiValue = Convert.toWei(_value, unit);
        final Function function = new Function("transfer", Arrays.asList(new Address(_to), new Uint256(weiValue)), Collections.emptyList());
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
        final List<TypeReference<?>> outputParam = Collections.singletonList(new TypeReference<Uint256>() {});
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
    public RemoteCall<RepTransactionReceipt> transferFrom(String _from, String _to, BigInteger _value, Convert.Unit unit, String remark) {
        BigInteger weiValue = Convert.toWei(_value, unit);
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

    public RemoteCall<String> name() {
        final List<Type> inputParam = Collections.emptyList();
        final List<TypeReference<?>> outputParam = Collections.singletonList(new TypeReference<Utf8String>() {});
        final Function function = new Function("name", inputParam, outputParam);
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> decimals() {
        final List<Type> inputParam = Collections.emptyList();
        final List<TypeReference<?>> outputParam = Collections.singletonList(new TypeReference<Uint8>() {});
        final Function function = new Function("decimals", inputParam, outputParam);
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> version() {
        final List<Type> inputParam = Collections.emptyList();
        final List<TypeReference<?>> outputParam = Collections.singletonList(new TypeReference<Utf8String>() {});
        final Function function = new Function("version", inputParam, outputParam);
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> symbol() {
        final List<Type> inputParam = Collections.emptyList();
        final List<TypeReference<?>> outputParam = Collections.singletonList(new TypeReference<Utf8String>() {});
        final Function function = new Function("symbol", inputParam, outputParam);
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<RepTransactionReceipt> approveAndCall(String _spender, BigInteger _value, byte[] _extraData) {
        final List<Type> inputParam = Arrays.asList(new Address(_spender), new Uint256(_value), new DynamicBytes(_extraData));
        final List<TypeReference<?>> outputParam = Collections.emptyList();
        final Function function = new Function("approveAndCall", inputParam, outputParam);
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<ERC20Token> deploy(Webuj webuj, Credentials credentials, BigInteger _price, BigInteger _limit, BigInteger _amount, String _name, BigInteger _units, String _symbol) {
        List<Type> param = Arrays.asList(new Uint256(_amount), new Utf8String(_name), new Uint8(_units), new Utf8String(_symbol));
        String encodedConstructor = FunctionEncoder.encodeConstructor(param);
        return deployRemoteCall(ERC20Token.class, webuj, credentials, _price, _limit, BINARY, encodedConstructor);
    }

    public static RemoteCall<ERC20Token> deploy(Webuj webuj, TransactionManager manager, BigInteger _price, BigInteger _limit, BigInteger _amount, String _name, BigInteger _units, String _symbol) {
        List<Type> param = Arrays.asList(new Uint256(_amount), new Utf8String(_name), new Uint8(_units), new Utf8String(_symbol));
        final String encodedConstructor = FunctionEncoder.encodeConstructor(param);
        return deployRemoteCall(ERC20Token.class, webuj, manager, _price, _limit, BINARY, encodedConstructor);
    }

    public static ERC20Token load(String contractAddress, Webuj webuj, Credentials credentials) {
        return new ERC20Token(contractAddress, webuj, credentials, GAS_PRICE, GAS_LIMIT);
    }

    public static ERC20Token load(String contractAddress, Webuj webuj, TransactionManager transactionManager) {
        return new ERC20Token(contractAddress, webuj, transactionManager, GAS_PRICE, GAS_LIMIT);
    }

}
