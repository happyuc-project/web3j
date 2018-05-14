package org.happyuc.webuj.console;

import java.io.File;
import java.io.IOException;

import org.happyuc.webuj.codegen.Console;
import org.happyuc.webuj.crypto.CipherException;
import org.happyuc.webuj.crypto.Credentials;
import org.happyuc.webuj.crypto.Keys;
import org.happyuc.webuj.crypto.WalletUtils;
import org.happyuc.webuj.utils.Files;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.utils.Files;

import static org.web3j.codegen.Console.exitError;
import static org.web3j.crypto.Keys.PRIVATE_KEY_LENGTH_IN_HEX;

/**
 * Create Ethereum wallet file from a provided private key.
 */
public class KeyImporter extends WalletManager {

    public KeyImporter() {
    }

    public KeyImporter(IODevice console) {
        super(console);
    }

    public static void main(String[] args) {
        if (args.length == 1) {
            new KeyImporter().run(args[0]);
        } else {
            new KeyImporter().run();
        }
    }

    static void main(IODevice console) {
        new KeyImporter(console).run();
    }

    private void run(String input) {
        File keyFile = new File(input);

        if (keyFile.isFile()) {
            String privateKey = null;
            try {
                privateKey = Files.readString(keyFile);
            } catch (IOException e) {
                Console.exitError("Unable to read file " + input);
            }

            createWalletFile(privateKey.trim());
        } else {
            createWalletFile(input.trim());
        }
    }

    private void run() {
        String input = console.readLine(
                "Please enter the hex encoded private key or key file location: ").trim();
        run(input);
    }

    private void createWalletFile(String privateKey) {
        if (!WalletUtils.isValidPrivateKey(privateKey)) {
            Console.exitError("Invalid private key specified, must be "
                    + Keys.PRIVATE_KEY_LENGTH_IN_HEX
                    + " digit hex value");
        }

        Credentials credentials = Credentials.create(privateKey);
        String password = getPassword("Please enter a wallet file password: ");

        String destinationDir = getDestinationDir();
        File destination = createDir(destinationDir);

        try {
            String walletFileName = WalletUtils.generateWalletFile(
                    password, credentials.getEcKeyPair(), destination, true);
            console.printf("Wallet file " + walletFileName
                    + " successfully created in: " + destinationDir + "\n");
        } catch (CipherException e) {
            Console.exitError(e);
        } catch (IOException e) {
            Console.exitError(e);
        }
    }
}
