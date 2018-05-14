package org.happyuc.webuj.protocol.scenarios;

import java.math.BigInteger;

import org.hamcrest.core.IsEqual;
import org.happyuc.webuj.generated.Fibonacci;
import org.happyuc.webuj.protocol.core.methods.response.RepTransactionReceipt;
import org.junit.Test;

import org.happyuc.webuj.protocol.WebujFactory;
import org.happyuc.webuj.protocol.http.HttpService;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Test Fibonacci contract generated wrappers.
 *
 * <p>Generated via running org.happyuc.webuj.codegen.SolidityFunctionWrapperGenerator with params:
 * <em>project-home</em>/src/test/resources/solidity/fibonacci.abi -o
 * <em>project-home</em>/src/integration-test/java -p org.happyuc.webuj.generated
 */
public class FunctionWrappersIT extends Scenario {

    @Test
    public void testFibonacci() throws Exception {
        Fibonacci fibonacci = Fibonacci.load("0x3c05b2564139fb55820b18b72e94b2178eaace7d", WebujFactory.build(new HttpService()), ALICE, GAS_PRICE, GAS_LIMIT);

        BigInteger result = fibonacci.fibonacci(BigInteger.valueOf(10)).send();
        assertThat(result, equalTo(BigInteger.valueOf(55)));
    }

    @Test
    public void testFibonacciNotify() throws Exception {
        Fibonacci fibonacci = Fibonacci.load("0x3c05b2564139fb55820b18b72e94b2178eaace7d", WebujFactory.build(new HttpService()), ALICE, GAS_PRICE, GAS_LIMIT);

        RepTransactionReceipt repTransactionReceipt = fibonacci.fibonacciNotify(BigInteger.valueOf(15)).send();

        Fibonacci.NotifyEventResponse result = fibonacci.getNotifyEvents(repTransactionReceipt).get(0);

        assertThat(result.input, IsEqual.<BigInteger>equalTo(BigInteger.valueOf(15)));

        assertThat(result.result, IsEqual.<BigInteger>equalTo(BigInteger.valueOf(610)));
    }
}
