/*
 * Copyright (C) 2024 Hedera Hashgraph, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import {ContractCallTestScenarioBuilder} from '../common.js';
import {PrecompileModificationTestTemplate} from '../commonPrecompileModificationFunctionsTemplate.js';

const contract = __ENV.ESTIMATE_PRECOMPILE_CONTRACT;
const selector = '0xa6218810'; //cryptoTransferExternal
const sender = __ENV.ACCOUNT_ADDRESS;
const receiver = __ENV.RECEIVER_ADDRESS;
const token = __ENV.TOKEN_ADDRESS;
const runMode = __ENV.RUN_WITH_VARIABLES;
//ABI encoded parameters used for crypto transfer token
const data1 =
  '0000000000000000000000000000000000000000000000000000000000000040' +
  '0000000000000000000000000000000000000000000000000000000000000080' +
  '0000000000000000000000000000000000000000000000000000000000000020' +
  '0000000000000000000000000000000000000000000000000000000000000000' +
  '0000000000000000000000000000000000000000000000000000000000000001' +
  '0000000000000000000000000000000000000000000000000000000000000020';
const data2 =
  '0000000000000000000000000000000000000000000000000000000000000060' +
  '0000000000000000000000000000000000000000000000000000000000000140' +
  '0000000000000000000000000000000000000000000000000000000000000002';
const data3 =
  'fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffd' +
  '0000000000000000000000000000000000000000000000000000000000000000';
const data4 =
  '0000000000000000000000000000000000000000000000000000000000000003' +
  '0000000000000000000000000000000000000000000000000000000000000000' +
  '0000000000000000000000000000000000000000000000000000000000000000';

//If RUN_WITH_VARIABLES=true will run tests with __ENV variables
const {options, run} =
  runMode === 'false'
    ? new PrecompileModificationTestTemplate('contractCallPrecompileCryptoTransferToken', false)
    : new ContractCallTestScenarioBuilder()
        .name('contractCallPrecompileCryptoTransferToken') // use unique scenario name among all tests
        .selector(selector)
        .args([data1, token, data2, sender, data3, receiver, data4])
        .to(contract)
        .build();

export {options, run};
