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

package org.aerogear.connectivity.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.aerogear.connectivity.jpa.PersistentObject;

@Entity
public class PushApplication extends PersistentObject {
    private static final long serialVersionUID = 6507691362454032282L;

    @Column
    private String name;
    @Column
    private String description;
    
    @Column
    private String pushApplicationID;

    // TODO: let's do LAZY
    @OneToMany(fetch=FetchType.EAGER)
    private Set<iOSVariant> iOSApps = new HashSet<iOSVariant>();

    // TODO: let's do LAZY
    @OneToMany(fetch=FetchType.EAGER)
    private Set<AndroidVariant> androidApps = new HashSet<AndroidVariant>();
    
    // TODO: let's do LAZY
    @OneToMany(fetch=FetchType.EAGER)
    private Set<SimplePushVariant> simplePushApps = new HashSet<SimplePushVariant>();
    
    
//    @OneToMany
//    private Set<AbstractMobileApplication> mobileApplications = new HashSet<AbstractMobileApplication>();


   public String getName() {
      return this.name;
   } 

   public void setName(final String name) {
      this.name = name;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(final String description) {
      this.description = description;
   }

//   public Set<AbstractMobileApplication> getMobileApplications() {
//       return mobileApplications;
//   }
//
//   public void setMobileApplications(Set<AbstractMobileApplication> mobileApplications) {
//       this.mobileApplications = mobileApplications;
//   }

   public Set<iOSVariant> getIOSApps() {
      return this.iOSApps;
   }

   public void setIOSApps(final Set<iOSVariant> iOSApps) {
      this.iOSApps = iOSApps;
   }

   public Set<AndroidVariant> getAndroidApps() {
      return this.androidApps;
   }

   public void setAndroidApps(final Set<AndroidVariant> androidApps) {
      this.androidApps = androidApps;
   }

   public Set<SimplePushVariant> getSimplePushApps() {
       return simplePushApps;
   }

   public void setSimplePushApps(final Set<SimplePushVariant> simplePushApps) {
       this.simplePushApps = simplePushApps;
   }

   public String getPushApplicationID() {
       return pushApplicationID;
   }

   public void setPushApplicationID(String pushApplicationID) {
       this.pushApplicationID = pushApplicationID;
   }
}
