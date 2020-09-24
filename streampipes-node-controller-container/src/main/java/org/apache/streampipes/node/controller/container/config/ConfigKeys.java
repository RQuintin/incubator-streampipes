package org.apache.streampipes.node.controller.container.config;
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

public class ConfigKeys {
    final static String NODE_CONTROLLER_ID_KEY = "SP_NODE_CONTROLLER_ID";
    final static String NODE_CONTROLLER_PORT_KEY = "SP_NODE_CONTROLLER_PORT";
    final static String NODE_HOST_KEY = "SP_NODE_HOST";
    final static String NODE_BROKER_HOST_KEY = "SP_NODE_BROKER_HOST";
    final static String NODE_BROKER_PORT_KEY = "SP_NODE_BROKER_PORT";
    final static String NODE_LOCATION_KEY = "SP_NODE_LOCATION";
    final static String NODE_HAS_GPU_KEY = "SP_NODE_HAS_GPU";
    final static String NODE_GPU_CUDA_CORES_KEY = "SP_NODE_GPU_CUDA_CORES";
    final static String NODE_GPU_TYPE_KEY = "SP_NODE_GPU_TYPE";
    final static String NODE_ACCESSIBLE_SENSOR_ACTUATOR_KEY = "SP_NODE_ACCESSIBLE_SENSOR_ACTUATOR";
    final static String NODE_SUPPORTED_PE_APP_ID_KEY = "SP_NODE_SUPPORTED_PE_APP_ID";
    final static String DOCKER_PRUNING_FREQ_SECS_KEY = "SP_DOCKER_PRUNING_FREQ_SECS";
    final static String NODE_RESOURCE_UPDATE_FREQ_SECS_KEY = "SP_NODE_RESOURCE_UPDATE_FREQ_SECS";

    final static String NODE_EVENT_BUFFER_SIZE = "SP_NODE_EVENT_BUFFER_SIZE";
}