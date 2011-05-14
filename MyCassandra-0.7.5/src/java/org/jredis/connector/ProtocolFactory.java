/*
 *   Copyright 2009 Joubin Houshyar
 * 
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *    
 *   http://www.apache.org/licenses/LICENSE-2.0
 *    
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.jredis.connector;

import org.jredis.NotSupportedException;
import org.jredis.protocol.Protocol;

/**
 * [TODO: document me!]
 *
 * @author  Joubin Houshyar (alphazero@sensesay.net)
 * @version alpha.0, 04/02/09
 * @since   alpha.0
 * 
 */
public interface ProtocolFactory {
	
	/**
	 * Creates a protocol handler for the redis version specified.
	 * @param redisVersion which the handler should support.  (e.g. "0.09")
	 * @return
	 * @throws NotSupportedException
	 */
	public Protocol createProtocolHandler (Connection.Modality modality, String redisVersion) throws NotSupportedException; 	
}
