/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.aerogear.connectivity.rest.registry.instances;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.aerogear.connectivity.api.MobileVariant;
import org.aerogear.connectivity.model.MobileVariantInstanceImpl;
import org.aerogear.connectivity.service.MobileVariantInstanceService;
import org.aerogear.connectivity.service.MobileVariantService;

@Stateless
@Path("/registry/device")
@TransactionAttribute
public class MobileVariantInstanceEndpoint
{
    @Inject private Logger logger;
    @Inject private MobileVariantInstanceService mobileApplicationInstanceService;
    @Inject private MobileVariantService mobileApplicationService;

    @POST
    @Consumes("application/json")
    public MobileVariantInstanceImpl registerInstallation(
            @HeaderParam("ag-mobile-variant") String mobileVariantID, 
            MobileVariantInstanceImpl entity) {
        if (logger.isLoggable(Level.INFO)) {
            StringBuilder sb = new StringBuilder();
            sb.append("ag-mobile-variant: ");
            sb.append(mobileVariantID);
            logger.info(sb.toString());
        }
        
        // find the matching variation:
        MobileVariant mobileApp = mobileApplicationService.findByVariantID(mobileVariantID);
        if (mobileApp == null) {
            logger.severe("\n\nCould not find Mobile Variant\n\n");
            return null; // TODO -> 404
        }
        
        List<MobileVariantInstanceImpl> instances = findInstanceByDeviceToken(mobileApp.getInstances(), entity.getDeviceToken());
        if (instances.isEmpty()) {
            // store the installation:
            entity = mobileApplicationInstanceService.addMobileVariantInstance(entity);
            

            // add installation to the matching variant
            mobileApplicationService.addInstance(mobileApp, entity);
        } else {
            logger.warning("UPDATE ON POST.......");
            
            // should be impossible
            if (instances.size()>1) {
                logger.severe("Too many registration for one installation");
            }
            
            // update the entity:
            entity = this.updateMobileApplicationInstance(instances.get(0), entity);
        }

        return entity;
   }
    
    // TODO: move to JQL
    private List<MobileVariantInstanceImpl> findInstanceByDeviceToken(Set<MobileVariantInstanceImpl> instances, String deviceToken) {
        final List<MobileVariantInstanceImpl> instancesWithToken = new ArrayList<MobileVariantInstanceImpl>();
        
        for (MobileVariantInstanceImpl instance : instances) {
            if (instance.getDeviceToken().equals(deviceToken))
                instancesWithToken.add(instance);
        }

        return instancesWithToken;
    }

    private MobileVariantInstanceImpl updateMobileApplicationInstance(MobileVariantInstanceImpl toUpdate, MobileVariantInstanceImpl postedVariant) {
        toUpdate.setCategory(postedVariant.getCategory());
        toUpdate.setDeviceToken(postedVariant.getDeviceToken());
        toUpdate.setAlias(postedVariant.getAlias());
        toUpdate.setDeviceType(postedVariant.getDeviceType());
        toUpdate.setMobileOperatingSystem(postedVariant.getMobileOperatingSystem());
        toUpdate.setOsVersion(postedVariant.getOsVersion());

        //update
        return mobileApplicationInstanceService.updateMobileVariantInstance(toUpdate);
    }

    @DELETE
    @Path("{token}")
    @Consumes("application/json")
    public void unregisterInstallations(
            @HeaderParam("ag-mobile-variant") String mobileVariantID, 
            @PathParam("token") String token) {
        
        // there can be multiple regs.........
        List<MobileVariantInstanceImpl> instances = mobileApplicationInstanceService.findMobileVariantInstancesByToken(token);
        
        // delete them:
        mobileApplicationInstanceService.removeMobileVariantInstances(instances);
   }
}