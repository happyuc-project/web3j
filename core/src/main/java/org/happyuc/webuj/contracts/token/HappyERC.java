package org.happyuc.webuj.contracts.token;

import org.happyuc.webuj.abi.EventEncoder;
import org.happyuc.webuj.abi.FunctionEncoder;
import org.happyuc.webuj.abi.TypeReference;
import org.happyuc.webuj.abi.datatypes.*;
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
import org.happyuc.webuj.tx.RawTransactionManager;
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

import static org.happyuc.webuj.protocol.core.methods.request.ReqTransaction.DEFAULT_GAS;

public class HappyERC extends Contract implements ERC20Interface {

    public static final String BINARY =
            "608060405260405162001c9938038062001c998339810180604052810190808051820192919060200180518201929190602001805190602001909291908051906020019092919080519060200190929190805190602001909291908051906020019092919050505060008260ff161180156200007e575060648260ff16105b15156200008a57600080fd5b6000841180156200009d57506008548410155b1515620000a957600080fd5b8660039080519060200190620000c1929190620002eb565b508560049080519060200190620000da929190620002eb565b50601260ff16600a0a8502600681905550836007819055508260088190555081600960006101000a81548160ff021916908360ff16021790555080600960016101000a81548160ff02191690831515021790555033600560006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506006546000803373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055506001600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008073ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055506001600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060003073ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff021916908315150217905550505050505050506200039a565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200032e57805160ff19168380011785556200035f565b828001600101855582156200035f579182015b828111156200035e57825182559160200191906001019062000341565b5b5090506200036e919062000372565b5090565b6200039791905b808211156200039357600081600090555060010162000379565b5090565b90565b6118ef80620003aa6000396000f300608060405260043610610106576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806271246c1461010b57806306fdde0314610138578063095ea7b3146101c8578063172a27b31461021557806318160ddd1461024457806318f4cdd01461026f5780631a46ec821461029a57806323b872dd14610311578063313ce5671461037e5780634d853ee5146103af57806351e6fb5e1461040657806370a08231146104af5780637e4bf747146105065780638cd0f1701461053157806395d89b41146105a0578063a530115e14610630578063a9059cbb146106ab578063d9caed12146106f8578063f0e2727414610765575b600080fd5b34801561011757600080fd5b5061013660048036038101908080359060200190929190505050610796565b005b34801561014457600080fd5b5061014d610962565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561018d578082015181840152602081019050610172565b50505050905090810190601f1680156101ba5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156101d457600080fd5b50610213600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610a00565b005b34801561022157600080fd5b5061022a610aea565b604051808215151515815260200191505060405180910390f35b34801561025057600080fd5b50610259610afd565b6040518082815260200191505060405180910390f35b34801561027b57600080fd5b50610284610b03565b6040518082815260200191505060405180910390f35b3480156102a657600080fd5b506102fb600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610b09565b6040518082815260200191505060405180910390f35b34801561031d57600080fd5b5061037c600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610b90565b005b34801561038a57600080fd5b50610393610da5565b604051808260ff1660ff16815260200191505060405180910390f35b3480156103bb57600080fd5b506103c4610daa565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561041257600080fd5b506104ad6004803603810190808035906020019082018035906020019080806020026020016040519081016040528093929190818152602001838360200280828437820191505050505050919291929080359060200190820180359060200190808060200260200160405190810160405280939291908181526020018383602002808284378201915050505050509192919290505050610dd0565b005b3480156104bb57600080fd5b506104f0600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610e3a565b6040518082815260200191505060405180910390f35b34801561051257600080fd5b5061051b610e82565b6040518082815260200191505060405180910390f35b34801561053d57600080fd5b5061059e600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803515159060200190929190505050610e88565b005b3480156105ac57600080fd5b506105b56110d8565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156105f55780820151818401526020810190506105da565b50505050905090810190601f1680156106225780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561063c57600080fd5b50610691600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050611176565b604051808215151515815260200191505060405180910390f35b3480156106b757600080fd5b506106f6600480360381019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035906020019092919050505061120a565b005b34801561070457600080fd5b50610763600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035906020019092919050505061127e565b005b34801561077157600080fd5b5061077a6115d4565b604051808260ff1660ff16815260200191505060405180910390f35b6000806000600560009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156107f757600080fd5b600960019054906101000a900460ff16151561081257600080fd5b60008411151561082157600080fd5b601260ff16600a0a8402925060065491506000803073ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054905082600660008282540192505081905550826000803073ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008282540192505081905550816006541115156108dc57fe5b806000803073ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205411151561092557fe5b7fd89206ebcf2cc92f1eddc19201874817588994f62ac00774965e3053188446f6846040518082815260200191505060405180910390a150505050565b60038054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156109f85780601f106109cd576101008083540402835291602001916109f8565b820191906000526020600020905b8154815290600101906020018083116109db57829003601f168201915b505050505081565b80600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508173ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925836040518082815260200191505060405180910390a35050565b600960019054906101000a900460ff1681565b60065481565b60075481565b6000600160008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054905092915050565b6000600160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054111515610c1b57600080fd5b80600160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205410151515610ca657600080fd5b610cb18383836115e7565b8173ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef836040518082815260200191505060405180910390a380600160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008282540392505081905550505050565b601281565b600560009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600081518351141515610de257600080fd5b600090505b8251811015610e3557610e288382815181101515610e0157fe5b906020019060200201518383815181101515610e1957fe5b9060200190602002015161120a565b8080600101915050610de7565b505050565b60008060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020549050919050565b60085481565b8015610eef57600560009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610eea57600080fd5b610fbf565b8273ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610f2957600080fd5b600260008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff161515610fbe57600080fd5b5b80600260008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055508173ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167f150c2647bf72a0ed3c83982af68fbdecab7fd32d4765ee560e45ef8377de600184604051808215151515815260200191505060405180910390a4505050565b60048054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561116e5780601f106111435761010080835404028352916020019161116e565b820191906000526020600020905b81548152906001019060200180831161115157829003601f168201915b505050505081565b6000600260008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff16905092915050565b6112153383836115e7565b8173ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef836040518082815260200191505060405180910390a35050565b6000600560009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614806113625750600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff165b151561136d57600080fd5b600073ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff161415611423576000821115156113b157600080fd5b813073ffffffffffffffffffffffffffffffffffffffff1631101515156113d757600080fd5b8373ffffffffffffffffffffffffffffffffffffffff166108fc839081150290604051600060405180830381858888f1935050505015801561141d573d6000803e3d6000fd5b50611569565b3073ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff161415611467576114628385846115e7565b611568565b60405180807f7472616e7366657228616464726573732c75696e7432353629000000000000008152506019019050604051809103902090508273ffffffffffffffffffffffffffffffffffffffff1662015f90827c010000000000000000000000000000000000000000000000000000000090049086856040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019250505060006040518083038160008887f19350505050151561156757600080fd5b5b5b8273ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff167f9b1bfa7fa9ee420a16e124f794c35ac9f90472acc99140eb2f6447c714cad8eb846040518082815260200191505060405180910390a350505050565b600960009054906101000a900460ff1681565b6000600754821115156115f957600080fd5b816000808673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020541015151561164657600080fd5b6000808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020549050816000808673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600082825403925050819055506116dc82611779565b9150816000808573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600082825401925050819055506000808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020548110151561177357fe5b50505050565b6000806000806000803073ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020549250600960009054906101000a900460ff1660ff16850291506064828115156117e357fe5b0490506007548110156117f65760075490505b6008548111156118065760085490505b8482101580156118165750808510155b151561182157600080fd5b806000803073ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600082825401925050819055506000803073ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054831015156118b657fe5b80850393505050509190505600a165627a7a72305820749ed68475485b0627a3f677c5a7eebe923a8584414d5fe94498b85600bf316c0029";

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

    public HappyERC(String contractAddress, Webuj webuj, TransactionManager transactionManager, BigInteger price,
                    BigInteger limit) {
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
        final Function function = new Function("transfer", Arrays.asList(new Address(_to), new Uint256(weiValue)),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public HucSendRepTransaction transfer2(String _to, String _value, String _remark) throws IOException {
        BigInteger weiValue = Convert.toWei(_value, Convert.Unit.HUC).toBigInteger();
        final Function function = new Function("transfer", Arrays.asList(new Address(_to), new Uint256(weiValue)),
                Collections.emptyList());
        TransactionData txData = new TransactionData(
                contractAddress,
                FunctionEncoder.encode(function),
                BigInteger.ZERO,
                gasPrice,
                gasLimit);
        return transactionManager.makeReqTransaction(txData).send();
    }

    @Override
    public RemoteCall<RepTransactionReceipt> mulTransfer(List<String> addresses, List<String> values,
                                                         List<String> remarks) {
        List<BigInteger> weiValues = values
                .stream()
                .map(_value -> Convert.toWei(_value, Convert.Unit.HUC).toBigInteger())
                .collect(Collectors.toList());
        DynamicArray<Address> dests =
                new DynamicArray<>(addresses.stream().map(Address::new).collect(Collectors.toList()));
        DynamicArray<Uint256> amounts =
                new DynamicArray<>(weiValues.stream().map(Uint256::new).collect(Collectors.toList()));

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
    public Observable<EventResponse.TransferEr> simpleTsEventObservable(DefaultBlockParameter startBlock,
                                                                        DefaultBlockParameter endBlock) {
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
    public <T> Observable<T> transferEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock
            , EventResponse.Rec<T> rec) {
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
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(APPROVAL_EVENT,
                repTransactionReceipt);
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
    public Observable<EventResponse.ApprovalEr> simpleAppEventObservable(DefaultBlockParameter startBlock,
                                                                         DefaultBlockParameter endBlock) {
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
    public <T> Observable<T> approvalEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock
            , EventResponse.Rec<T> rec) {
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
        final List<Type> inputParam = Arrays.asList(new Address(_spender), new Uint256(_value),
                new DynamicBytes(_extraData));
        final List<TypeReference<?>> outputParam = Collections.emptyList();
        final Function function = new Function("approveAndCall", inputParam, outputParam);
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<HappyERC> deploy(
            Webuj webuj,
            Credentials credentials,
            BigInteger price,
            BigInteger limit,
            BigInteger value,
            String name,
            String symbol,
            long supply,
            long costmin,
            long costmax,
            long costpc,
            boolean extend) {
        return deploy(webuj, new RawTransactionManager(webuj, credentials), price, limit,value,
                name, symbol, supply, costmin, costmax, costpc, extend);
    }

    public static RemoteCall<HappyERC> deploy(
            Webuj webuj,
            TransactionManager manager,
            BigInteger price,
            BigInteger limit,
            BigInteger value,
            String name,
            String symbol,
            long supply,
            long costmin,
            long costmax,
            long costpc,
            boolean extend) {
        List<Type> param = Arrays.asList(
                new Utf8String(name),
                new Utf8String(symbol),
                new Uint256(supply),
                new Uint256(costmin),
                new Uint256(costmax),
                new Uint8(costpc),
                new Bool(extend));
        String encodedConstructor = FunctionEncoder.encodeConstructor(param);
        return deployRemoteCall(HappyERC.class, webuj, manager, price, limit, BINARY, encodedConstructor, value);
    }

    public static HappyERC load(String contractAddress, Webuj webuj, Credentials credentials) {
        return load(contractAddress, webuj, new RawTransactionManager(webuj, credentials));
    }

    public static HappyERC load(String contractAddress, Webuj webuj, TransactionManager transactionManager) {
        return new HappyERC(contractAddress, webuj, transactionManager, GAS_PRICE, DEFAULT_GAS);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
