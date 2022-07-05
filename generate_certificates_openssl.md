# Generating Root Certificate (Self signed)

Generate a 4096-bit RSA private key. Protected by password (rootca), file generate (pw_rootca_privateKey.key)
> openssl genrsa -des3 -out rootca_pK.key 2048

Generate a certificate signing request (CSR).
> openssl req -key rootca_pK.key -new -out rootca.csr

Sign the certificate request with the private key
> openssl x509 -signkey rootca_pK.key -in rootca.csr -req -days 1825 -out rootca.crt

View the ceritificate
> openssl x509 -text -noout -in rootca.crt

Convert to CRT to PEM
> openssl x509 -in rootca.crt -out rootca.pem -outform PEM

Convert key from PKCS#1 to PKCS#8
> openssl pkcs8 -topk8 -inform PEM -outform DER -in rootca_pK.key -out rootca_pK.pkcs8 -nocrypt

# Generating Intermediate Certificate
> openssl genrsa -des3 -out intermediate_pK.key 2048

> openssl req -key intermediate_pK.key -new -out intermediate.csr

Sign with the Root Certificate
> openssl x509 -req -days 1825 -in intermediate.csr -CA ../rootca/rootca.crt -CAkey ../rootca/rootca_pK.key -set_serial 01 -out intermediate.crt

> openssl x509 -text -noout -in intermediate.crt

> openssl x509 -in intermediate.crt -out intermediate.pem -outform PEM

> openssl pkcs8 -topk8 -inform PEM -outform DER -in intermediate_pK.key -out intermediate_pK.pkcs8 -nocrypt

# Generating the Server Certificate from Root CA
> openssl genrsa -des3 -out server_pK.key 2048

> openssl req -key server_pK.key -new -out server.csr

Sign with the Root Certificate
> openssl x509 -req -days 1825 -in server.csr -CA ../rootca/rootca.crt -CAkey ../rootca/rootca_pK.key -set_serial 01 -out server.crt

> openssl x509 -text -noout -in server.crt

> openssl x509 -in server.crt -out server.pem -outform PEM

> openssl pkcs8 -topk8 -inform PEM -outform DER -in server_pK.key -out server_pK.pkcs8 -nocrypt

# Generating a Client Cerftificate from Intermediate CA
> openssl genrsa -des3 -out client_pK.key 2048

> openssl req -key client_pK.key -new -out client.csr

Sign with the Intermediate Certificate
> openssl x509 -req -days 1825 -in client.csr -CA ../intermediate/intermediate.crt -CAkey ../intermediate/intermediate_pK.key -set_serial 01 -out client.crt

> openssl x509 -text -noout -in client.crt

> openssl x509 -in client.crt -out client.pem -outform PEM

> openssl pkcs8 -topk8 -inform PEM -outform DER -in client_pK.key -out client_pK.pkcs8 -nocrypt

