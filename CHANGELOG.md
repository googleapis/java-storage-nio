# Changelog

## [0.122.0](https://www.github.com/googleapis/java-storage-nio/compare/v0.121.2...v0.122.0) (2020-11-06)


### Features

* update cloudstorageconfiguration to improve copy accross cross-bucket performance ([#168](https://www.github.com/googleapis/java-storage-nio/issues/168)) ([db74524](https://www.github.com/googleapis/java-storage-nio/commit/db74524d68487df71c80b122d8c0ff384dc9ace3))
* **deps:** adopt flatten plugin and google-cloud-shared-dependencies ([#156](https://www.github.com/googleapis/java-storage-nio/issues/156)) ([510f6a5](https://www.github.com/googleapis/java-storage-nio/commit/510f6a5efc4a13b010020a8d67ec1511bbd46564))


### Bug Fixes

* FakeStorageRpc#list to filtering the files by bucket and prefix ([#208](https://www.github.com/googleapis/java-storage-nio/issues/208)) ([21f606e](https://www.github.com/googleapis/java-storage-nio/commit/21f606eca67b1c8a471ee28f7e2dd3851a0c493e))
* implement writeWithResponse in FakeStorageRpc ([#187](https://www.github.com/googleapis/java-storage-nio/issues/187)) ([10ddfae](https://www.github.com/googleapis/java-storage-nio/commit/10ddfae3923f1d5e64b151293db238f1af433d03))


### Dependencies

* update dependency com.google.apis:google-api-services-storage to v1-rev20200611-1.30.9 ([#166](https://www.github.com/googleapis/java-storage-nio/issues/166)) ([3cab5f2](https://www.github.com/googleapis/java-storage-nio/commit/3cab5f25e081c0198d06903a42c7de3dbd34bbad))
* update dependency com.google.apis:google-api-services-storage to v1-rev20200727-1.30.10 ([#171](https://www.github.com/googleapis/java-storage-nio/issues/171)) ([62998f0](https://www.github.com/googleapis/java-storage-nio/commit/62998f0adfb22b194e2dd633532e13bf27a79479))
* update dependency com.google.apis:google-api-services-storage to v1-rev20200814-1.30.10 ([#206](https://www.github.com/googleapis/java-storage-nio/issues/206)) ([0d7cd44](https://www.github.com/googleapis/java-storage-nio/commit/0d7cd44e73b0b9456a8ba96cc6e6d0e60a0b7d4e))
* update dependency com.google.apis:google-api-services-storage to v1-rev20200927-1.30.10 ([#233](https://www.github.com/googleapis/java-storage-nio/issues/233)) ([91b7918](https://www.github.com/googleapis/java-storage-nio/commit/91b7918539186763ecaa377cfb6f3908105df279))
* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.10.0 ([#226](https://www.github.com/googleapis/java-storage-nio/issues/226)) ([83acbd7](https://www.github.com/googleapis/java-storage-nio/commit/83acbd766b06d66fc9a41385fdd49aa57d9a0291))
* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.10.1 ([#239](https://www.github.com/googleapis/java-storage-nio/issues/239)) ([bd1928d](https://www.github.com/googleapis/java-storage-nio/commit/bd1928da234dae6fc2fb1ad6658d89a9ebc7af82))
* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.10.2 ([#243](https://www.github.com/googleapis/java-storage-nio/issues/243)) ([4ca9869](https://www.github.com/googleapis/java-storage-nio/commit/4ca9869d99fc3d2e578fb86fbd9053f855f1e51c))
* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.12.0 ([#246](https://www.github.com/googleapis/java-storage-nio/issues/246)) ([3e6171b](https://www.github.com/googleapis/java-storage-nio/commit/3e6171b2d4c4c7832b62288faf30f767c5278762))
* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.12.1 ([#255](https://www.github.com/googleapis/java-storage-nio/issues/255)) ([73d7dd2](https://www.github.com/googleapis/java-storage-nio/commit/73d7dd2ec6ca371480955c844ffb5e6aed4f608f))
* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.13.0 ([#260](https://www.github.com/googleapis/java-storage-nio/issues/260)) ([54895cf](https://www.github.com/googleapis/java-storage-nio/commit/54895cf441f1e1723cf941aa1f6cab249acae6b8))
* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.14.1 ([#271](https://www.github.com/googleapis/java-storage-nio/issues/271)) ([058d7c9](https://www.github.com/googleapis/java-storage-nio/commit/058d7c9c1471846f3d897cac31cb5f6d42ce5a7d))
* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.8.2 ([#167](https://www.github.com/googleapis/java-storage-nio/issues/167)) ([3b14bbc](https://www.github.com/googleapis/java-storage-nio/commit/3b14bbc05d5658c57d4f272fdfdf152d7d9ee18d))
* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.8.3 ([#169](https://www.github.com/googleapis/java-storage-nio/issues/169)) ([4e7bac1](https://www.github.com/googleapis/java-storage-nio/commit/4e7bac10c2466d1407e8f1a6de838261e6c1b097))
* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.8.4 ([#189](https://www.github.com/googleapis/java-storage-nio/issues/189)) ([af492b8](https://www.github.com/googleapis/java-storage-nio/commit/af492b8068101727793e64e89da2778205fa08b6))
* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.8.6 ([#191](https://www.github.com/googleapis/java-storage-nio/issues/191)) ([9bbc1fc](https://www.github.com/googleapis/java-storage-nio/commit/9bbc1fc755271f2ac6b798d50f6c0ba8e022c589))
* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.9.0 ([#202](https://www.github.com/googleapis/java-storage-nio/issues/202)) ([cf312a3](https://www.github.com/googleapis/java-storage-nio/commit/cf312a3fe46ec446d1b5ee418849b3ab9abee87e))
* update dependency com.google.cloud:google-cloud-storage to v1.110.0 ([#154](https://www.github.com/googleapis/java-storage-nio/issues/154)) ([fd6de38](https://www.github.com/googleapis/java-storage-nio/commit/fd6de38ee63376afff03c7e794dce1f4524a4375))
* update dependency com.google.cloud:google-cloud-storage to v1.112.0 ([#199](https://www.github.com/googleapis/java-storage-nio/issues/199)) ([8a38817](https://www.github.com/googleapis/java-storage-nio/commit/8a3881732551bcef1d76f76026a29071fc50dd43))
* update dependency com.google.cloud:google-cloud-storage to v1.113.0 ([#205](https://www.github.com/googleapis/java-storage-nio/issues/205)) ([068002e](https://www.github.com/googleapis/java-storage-nio/commit/068002e1145418ecb848ed066346d1fc6ec14807))
* update dependency com.google.cloud:google-cloud-storage to v1.113.1 ([#212](https://www.github.com/googleapis/java-storage-nio/issues/212)) ([acc1fe8](https://www.github.com/googleapis/java-storage-nio/commit/acc1fe8f23c5926000c7a869ca9a71372850c46a))
* update dependency com.google.cloud:google-cloud-storage to v1.113.2 ([#266](https://www.github.com/googleapis/java-storage-nio/issues/266)) ([9d2c11d](https://www.github.com/googleapis/java-storage-nio/commit/9d2c11d009394f3f3ffd1b115b97b350aa64d1b7))
* update dependency com.google.guava:guava to v30 ([#263](https://www.github.com/googleapis/java-storage-nio/issues/263)) ([4e81dab](https://www.github.com/googleapis/java-storage-nio/commit/4e81daba421e0e42695203c6fe74670b32a3f576))
* update dependency org.mockito:mockito-core to v3.4.4 ([#170](https://www.github.com/googleapis/java-storage-nio/issues/170)) ([3e06bd5](https://www.github.com/googleapis/java-storage-nio/commit/3e06bd5b07065acb1b41a03ebbb862487c046eeb))
* update dependency org.mockito:mockito-core to v3.5.10 ([#203](https://www.github.com/googleapis/java-storage-nio/issues/203)) ([33fdc31](https://www.github.com/googleapis/java-storage-nio/commit/33fdc315a5b4bccdebb13262cbc5868011abe444))
* update dependency org.mockito:mockito-core to v3.5.11 ([#214](https://www.github.com/googleapis/java-storage-nio/issues/214)) ([575c308](https://www.github.com/googleapis/java-storage-nio/commit/575c30882ab84e1b2b1ce34d5176b4f8688a8136))
* update dependency org.mockito:mockito-core to v3.5.7 ([#194](https://www.github.com/googleapis/java-storage-nio/issues/194)) ([8cc7616](https://www.github.com/googleapis/java-storage-nio/commit/8cc76166baf3f5f49223bb8eafc8704679c4d427))

### [0.121.2](https://www.github.com/googleapis/java-storage-nio/compare/v0.120.1...v0.121.2) (2020-06-18)


### Bug Fixes

* update FakeStorageRpc to extend StorageRpcTestBase [#135](https://www.github.com/googleapis/java-storage-nio/issues/135) ([7614295](https://www.github.com/googleapis/java-storage-nio/commit/761429571eea15b11b2d44b4f5a2c65b4f649484))

### [0.121.1](https://www.github.com/googleapis/java-storage-nio/compare/v0.120.0...v0.121.1) (2020-06-16)


### Dependencies

* update dependency com.google.apis:google-api-services-storage to v1-rev20200410-1.30.9 ([#97](https://www.github.com/googleapis/java-storage-nio/issues/97)) ([b86aed8](https://www.github.com/googleapis/java-storage-nio/commit/b86aed82b7959f2866d3430a4ab79f5983ea6e6f))
* update dependency com.google.apis:google-api-services-storage to v1-rev20200430-1.30.9 ([#110](https://www.github.com/googleapis/java-storage-nio/issues/110)) ([9350ed4](https://www.github.com/googleapis/java-storage-nio/commit/9350ed430c656fc3622025299008de934c80b99d))
* update dependency com.google.auto.value:auto-value to v1.7.1 ([#98](https://www.github.com/googleapis/java-storage-nio/issues/98)) ([3f07925](https://www.github.com/googleapis/java-storage-nio/commit/3f07925cb97b578774d721640588a83d3426b58d))
* update dependency com.google.auto.value:auto-value to v1.7.2 ([#104](https://www.github.com/googleapis/java-storage-nio/issues/104)) ([ff05184](https://www.github.com/googleapis/java-storage-nio/commit/ff05184b1c151ebea2dd45a818cdf5d65f437adc))
* update dependency com.google.auto.value:auto-value to v1.7.3 ([#131](https://www.github.com/googleapis/java-storage-nio/issues/131)) ([a225b87](https://www.github.com/googleapis/java-storage-nio/commit/a225b878e793e111390ce9e403b2a0018cff7c4c))
* update dependency com.google.auto.value:auto-value-annotations to v1.7.1 ([#99](https://www.github.com/googleapis/java-storage-nio/issues/99)) ([6aef5d6](https://www.github.com/googleapis/java-storage-nio/commit/6aef5d60f1d30e861b5ca6899f4942c3831c1bf8))
* update dependency com.google.auto.value:auto-value-annotations to v1.7.2 ([#105](https://www.github.com/googleapis/java-storage-nio/issues/105)) ([c0f3611](https://www.github.com/googleapis/java-storage-nio/commit/c0f36119bafbef4536f2ec66a8c417fe3eae624f))
* update dependency com.google.auto.value:auto-value-annotations to v1.7.3 ([#132](https://www.github.com/googleapis/java-storage-nio/issues/132)) ([3a46dd5](https://www.github.com/googleapis/java-storage-nio/commit/3a46dd59a3777aea490e6100a20425183d982cb3))
* update dependency com.google.cloud:google-cloud-core-bom to v1.93.5 ([#115](https://www.github.com/googleapis/java-storage-nio/issues/115)) ([7088962](https://www.github.com/googleapis/java-storage-nio/commit/70889626de81793e19c85b8c886dbea3456449d0))
* update dependency com.google.cloud:google-cloud-storage to v1.108.0 ([#100](https://www.github.com/googleapis/java-storage-nio/issues/100)) ([6a9a281](https://www.github.com/googleapis/java-storage-nio/commit/6a9a28105561d4408110d54bb92d4e1099ed7544))
* update dependency com.google.cloud:google-cloud-storage to v1.109.0 ([#133](https://www.github.com/googleapis/java-storage-nio/issues/133)) ([cb23faf](https://www.github.com/googleapis/java-storage-nio/commit/cb23faf0132db61bfdc8d0c6409dc9fa98ae2d11))
* update dependency com.google.http-client:google-http-client-bom to v1.35.0 ([#93](https://www.github.com/googleapis/java-storage-nio/issues/93)) ([0683dd2](https://www.github.com/googleapis/java-storage-nio/commit/0683dd21731b86d22a9d9f7201dad9deb1878d8a))
* update dependency com.google.protobuf:protobuf-bom to v3.12.0 ([#111](https://www.github.com/googleapis/java-storage-nio/issues/111)) ([4e592fb](https://www.github.com/googleapis/java-storage-nio/commit/4e592fb506a9b85c8d38c662605d4f97ad60b730))
* update dependency com.google.protobuf:protobuf-bom to v3.12.1 ([#114](https://www.github.com/googleapis/java-storage-nio/issues/114)) ([1b9b5e5](https://www.github.com/googleapis/java-storage-nio/commit/1b9b5e5a780f44abcba2b7eb25a2a46f5bf8c1ba))
* update dependency com.google.protobuf:protobuf-bom to v3.12.2 ([#116](https://www.github.com/googleapis/java-storage-nio/issues/116)) ([c297693](https://www.github.com/googleapis/java-storage-nio/commit/c297693dee0747d259ff1bcbc40d05218a782709))


### Documentation

* update CONTRIBUTING.md to include code formatting ([#534](https://www.github.com/googleapis/java-storage-nio/issues/534)) ([#103](https://www.github.com/googleapis/java-storage-nio/issues/103)) ([c329a58](https://www.github.com/googleapis/java-storage-nio/commit/c329a58086501f77d7c7e0db44eb06cb68eb933b))

## [0.121.0](https://www.github.com/googleapis/java-storage-nio/compare/0.120.0-alpha...v0.121.0) (2020-04-24)


### Features

* make repo releasable, add parent pom ([#6](https://www.github.com/googleapis/java-storage-nio/issues/6)) ([6bca496](https://www.github.com/googleapis/java-storage-nio/commit/6bca49650fa5bac4682836149c48db95908909a5))


### Bug Fixes

* point to correct api documentation ([#68](https://www.github.com/googleapis/java-storage-nio/issues/68)) ([43675b6](https://www.github.com/googleapis/java-storage-nio/commit/43675b6416d9bec224704d92b8b9abf1fc9db10b))


### Dependencies

* update core dependencies ([#27](https://www.github.com/googleapis/java-storage-nio/issues/27)) ([b59ae15](https://www.github.com/googleapis/java-storage-nio/commit/b59ae1587bc08714c549b4d22dc078e16ef48d98))
* update core dependencies to v29 ([#78](https://www.github.com/googleapis/java-storage-nio/issues/78)) ([63d9a56](https://www.github.com/googleapis/java-storage-nio/commit/63d9a56dc0a4e7c7e1eb9e83a6fda6d715d82edb))
* update dependency com.google.apis:google-api-services-storage to v1-rev20191127-1.30.8 ([#33](https://www.github.com/googleapis/java-storage-nio/issues/33)) ([bd4d1b2](https://www.github.com/googleapis/java-storage-nio/commit/bd4d1b2c6dfdf2497f68fb328778e751c3a0813a))
* update dependency com.google.apis:google-api-services-storage to v1-rev20191127-1.30.9 ([#48](https://www.github.com/googleapis/java-storage-nio/issues/48)) ([6a63920](https://www.github.com/googleapis/java-storage-nio/commit/6a63920d440229b0657f5464b122c3eb8bb6d882))
* update dependency com.google.apis:google-api-services-storage to v1-rev20200226-1.30.9 ([#53](https://www.github.com/googleapis/java-storage-nio/issues/53)) ([5319c7e](https://www.github.com/googleapis/java-storage-nio/commit/5319c7e63de772983884d9d2e275102aab30055c))
* update dependency com.google.apis:google-api-services-storage to v1-rev20200326-1.30.9 ([#77](https://www.github.com/googleapis/java-storage-nio/issues/77)) ([315cc86](https://www.github.com/googleapis/java-storage-nio/commit/315cc8660bf670098c498397f6c7f6b7b9d4376c))
* update dependency com.google.cloud:google-cloud-core-bom to v1.93.3 ([#43](https://www.github.com/googleapis/java-storage-nio/issues/43)) ([58a7c03](https://www.github.com/googleapis/java-storage-nio/commit/58a7c038fa3bd2b72486aa40019153004b7e7958))
* update dependency com.google.cloud:google-cloud-storage to v1.104.0 ([#26](https://www.github.com/googleapis/java-storage-nio/issues/26)) ([020d7cf](https://www.github.com/googleapis/java-storage-nio/commit/020d7cf350d05dfd30be14ad7aab8ee051f4797f))
* update dependency com.google.cloud:google-cloud-storage to v1.105.2 ([#44](https://www.github.com/googleapis/java-storage-nio/issues/44)) ([1bfa769](https://www.github.com/googleapis/java-storage-nio/commit/1bfa7697d39f7856defe997da1016986632be4cd))
* update dependency com.google.cloud:google-cloud-storage to v1.106.0 ([#56](https://www.github.com/googleapis/java-storage-nio/issues/56)) ([57fff87](https://www.github.com/googleapis/java-storage-nio/commit/57fff872878942210db056974132c4123ae08e0b))
* update dependency com.google.cloud.samples:shared-configuration to v1.0.13 ([#63](https://www.github.com/googleapis/java-storage-nio/issues/63)) ([ecac9a9](https://www.github.com/googleapis/java-storage-nio/commit/ecac9a9a839cfe9649ab53b5eb675c16ddeeea6a))
* update dependency com.google.guava:guava to v23 ([#52](https://www.github.com/googleapis/java-storage-nio/issues/52)) ([ef0baaa](https://www.github.com/googleapis/java-storage-nio/commit/ef0baaa6805b0fa57854af8ae903262c55ee7d5d))
* update dependency com.google.http-client:google-http-client-bom to v1.34.2 ([#25](https://www.github.com/googleapis/java-storage-nio/issues/25)) ([c1c3269](https://www.github.com/googleapis/java-storage-nio/commit/c1c326989940f660df0095b6a0efe3253c2da1b0))
* update dependency com.google.protobuf:protobuf-bom to v3.11.4 ([#30](https://www.github.com/googleapis/java-storage-nio/issues/30)) ([ddd0555](https://www.github.com/googleapis/java-storage-nio/commit/ddd05553cb5af81b0a56277076c81a2fa4ad1abd))
