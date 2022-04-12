package com.apemans.smartipcimpl.ui.ipcdevices.player.old.bean;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author:dongbeihu
 * @Description:
 * @Date: 2021/12/15-20:55
 */

public enum IpcType {
    IPC_720("IPC007-720P"),
    IPC_1080("IPC007-1080P"),
    IPC_100("IPC100"),
    IPC_200("IPC200"),
    IPC_300_CAM("IPC300-CAM"),
    IPC_300_HUB("IPC300-HUB"),
    PC420("PC420"),
    PC440("PC440"),
    PC530("PC530"),
    PC530PRO("PC530PRO"),
    PC540("PC540"),
    PC650("PC650"),
    PC660("PC660"),
    PC660PRO("PC660PRO"),
    SC100("SC100"),
    SC210("SC210"),
    SC220("SC220"),
    PC730("PC730"),
    PC770("PC770"),
    EC810PRO("EC810PRO"),
    EC810_PLUS("EC810-PLUS"),
    EC810_CAM("EC810-CAM"),
    EC810_HUB("EC810-HUB"),
    MC120("MC120"),
    TC100("TC100"),
    TR100("TR100"),
    TS100("TS100"),
    TS200("TS200"),
    HC320("HC320"),
    C1("C1"),
    A1("C2"),
    P3("P3"),
    P3Pro("P3Pro"),
    P1("P1"),
    P2("P2"),
    P4("P4"),
    K1("K1"),
    K2("K2"),
    Q1("Q1"),
    T1("T1"),
    M1("M1"),
    W0_CAM("W0-CAM"),
    W0_HUB("W1-HUB"),
    W1("W1-CAM"),
    W2("W2"),
    XC100("XC100"),
    IPC_UNKNOWN("IPC-UNKNOWN");

    public static final String IPC_720_TYPE = "IPC007-720P";
    public static final String IPC_1080_TYPE = "IPC007-1080P";
    public static final String IPC_100_TYPE = "IPC100";
    public static final String IPC_200_TYPE = "IPC200";
    public static final String IPC_300_CAM_TYPE = "IPC300-CAM";
    public static final String IPC_300_HUB_TYPE = "IPC300-HUB";
    public static final String IPC_720_TEST_TYPE = "IPC007-720P_TEST";
    public static final String IPC_1080_TEST_TYPE = "IPC007-1080P_TEST";
    public static final String IPC_100_TEST_TYPE = "IPC100_TEST";
    public static final String IPC_200_TEST_TYPE = "IPC200_TEST";
    public static final String NOOIE_720P_TYPE = "720P";
    public static final String NOOIE_1080P_TYPE = "1080P";
    public static final String IPC007A_1080P_TYPE = "IPC007A-1080P";
    public static final String IPC100A_TYPE = "IPC100A";
    public static final String IPC200A_TYPE = "IPC200A";
    public static final String PC420_TYPE = "PC420";
    public static final String PC420F_TYPE = "PC420F";
    public static final String PC440_TYPE = "PC440";
    public static final String PC530_TYPE = "PC530";
    public static final String PC530F37_TYPE = "PC530F37";
    public static final String PC530PRO_TYPE = "PC530PRO";
    public static final String PC530A_TYPE = "PC530A";
    public static final String PC530B_TYPE = "PC530B";
    public static final String PC540_TYPE = "PC540";
    public static final String PC650_TYPE = "PC650";
    public static final String PC660_TYPE = "PC660";
    public static final String PC660PRO_TYPE = "PC660PRO";
    public static final String SC100_TYPE = "SC100";
    public static final String SC100F23_TYPE = "SC100F23";
    public static final String SC210_TYPE = "SC210";
    public static final String SC210F23_TYPE = "SC210F23";
    public static final String SC220_TYPE = "SC220";
    public static final String SC220F37_TYPE = "SC220F37";
    public static final String PC730_TYPE = "PC730";
    public static final String PC730F23_TYPE = "PC730F23";
    public static final String PC730A_TYPE = "PC730A";
    public static final String PC770_TYPE = "PC770";
    public static final String PC770F37_TYPE = "PC770F37";
    public static final String EC810PLUS_TYPE = "EC810-PLUS";
    public static final String EC810PRO_TYPE = "EC810PRO";
    public static final String EC810_CAM_TYPE = "EC810-CAM";
    public static final String EC810_HUB_TYPE = "EC810-HUB";
    public static final String MC120_TYPE = "MC120";
    public static final String TC100_TYPE = "TC100";
    public static final String TR100_TYPE = "TR100";
    public static final String TS100_TYPE = "TS100";
    public static final String TS200_TYPE = "TS200";
    public static final String HC320_TYPE = "HC320";
    public static final String C1_TYPE = "C1";
    public static final String A1_TYPE = "C2";
    public static final String P3_TYPE = "P3";
    public static final String P3PRO_TYPE = "P3Pro";
    public static final String P1_TYPE = "P1";
    public static final String P2_TYPE = "P2";
    public static final String P4_TYPE = "P4";
    public static final String K1_TYPE = "K1";
    public static final String K2_TYPE = "K2";
    public static final String Q1_TYPE = "Q1";
    public static final String T1_TYPE = "T1";
    public static final String M1_TYPE = "M1";
    public static final String W0_CAM_TYPE = "W0-CAM";
    public static final String W0_HUB_TYPE = "W1-HUB";
    public static final String W1_TYPE = "W1-CAM";
    public static final String W2_TYPE = "W2";
    public static final String XC100_TYPE = "XC100";
    private static final String COMPATIBLE_FACTOR_A = "A";
    private static final String COMPATIBLE_FACTOR_F = "F";
    private String type;

    private IpcType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public static IpcType getIpcType(String type) {
        type = TextUtils.isEmpty(type) ? type : type.toUpperCase();
        if (!"IPC007-720P".equalsIgnoreCase(type) && !"IPC007-720P_TEST".equalsIgnoreCase(type) && !"720P".equalsIgnoreCase(type) && !checkCompatibleIpc720(type)) {
            if (!"IPC007-1080P".equalsIgnoreCase(type) && !"IPC007-1080P_TEST".equalsIgnoreCase(type) && !"1080P".equalsIgnoreCase(type) && !"IPC007A-1080P".equalsIgnoreCase(type) && !checkCompatibleIpc1080(type)) {
                if (!"IPC100".equalsIgnoreCase(type) && !"IPC100_TEST".equalsIgnoreCase(type) && !"IPC100A".equalsIgnoreCase(type) && !checkCompatibleIpc100(type)) {
                    if (!"IPC200".equalsIgnoreCase(type) && !"IPC200_TEST".equalsIgnoreCase(type) && !"IPC200A".equalsIgnoreCase(type) && !checkCompatibleIpc200(type)) {
                        if (!"IPC300-CAM".equalsIgnoreCase(type) && !checkCompatibleIpc300Cam(type)) {
                            if (!"IPC300-HUB".equalsIgnoreCase(type) && !checkCompatibleIpc300Hub(type)) {
                                if (!"PC420".equalsIgnoreCase(type) && !checkCompatiblePc420(type)) {
                                    if (!"PC440".equalsIgnoreCase(type) && !checkCompatiblePc440(type)) {
                                        if (!"PC530".equalsIgnoreCase(type) && !"PC530A".equalsIgnoreCase(type) && !"PC530F37".equalsIgnoreCase(type) && !checkCompatiblePc530(type)) {
                                            if (checkCompatiblePc530Pro(type)) {
                                                return PC530PRO;
                                            } else if (!"PC540".equalsIgnoreCase(type) && !checkCompatiblePc540(type)) {
                                                if (!"PC650".equalsIgnoreCase(type) && !checkCompatiblePc650(type)) {
                                                    if (checkCompatiblePc660(type)) {
                                                        return PC660;
                                                    } else if (checkCompatiblePc660Pro(type)) {
                                                        return PC660PRO;
                                                    } else if (!"PC730".equalsIgnoreCase(type) && !"PC730A".equalsIgnoreCase(type) && !"PC730F23".equalsIgnoreCase(type) && !checkCompatiblePc730(type)) {
                                                        if (!"PC770".equalsIgnoreCase(type) && !"PC770F37".equalsIgnoreCase(type) && !checkCompatiblePc770(type)) {
                                                            if (checkCompatibleSc100(type)) {
                                                                return SC100;
                                                            } else if (!"SC210".equalsIgnoreCase(type) && !"SC210F23".equalsIgnoreCase(type) && !checkCompatibleSc210(type)) {
                                                                if (!"SC220".equalsIgnoreCase(type) && !"SC220F37".equalsIgnoreCase(type) && !checkCompatibleSc220(type)) {
                                                                    if (!"EC810-PLUS".equalsIgnoreCase(type) && !checkCompatibleEc810Plus(type)) {
                                                                        if (!"EC810PRO".equalsIgnoreCase(type) && !checkCompatibleEc810Pro(type)) {
                                                                            if (!"EC810-CAM".equalsIgnoreCase(type) && !checkCompatibleEc810Cam(type)) {
                                                                                if (!"EC810-HUB".equalsIgnoreCase(type) && !checkCompatibleEc810Hub(type)) {
                                                                                    if (!"MC120".equalsIgnoreCase(type) && !checkCompatibleMc120(type)) {
                                                                                        if (!"TC100".equalsIgnoreCase(type) && !checkCompatibleTc100(type)) {
                                                                                            if (!"TR100".equalsIgnoreCase(type) && !checkCompatibleTr100(type)) {
                                                                                                if (!"TS100".equalsIgnoreCase(type) && !checkCompatibleTs100(type)) {
                                                                                                    if (checkCompatibleTs200(type)) {
                                                                                                        return TS200;
                                                                                                    } else if (checkCompatibleHc320(type)) {
                                                                                                        return HC320;
                                                                                                    } else if (checkCompatibleC1(type)) {
                                                                                                        return C1;
                                                                                                    } else if (checkCompatibleA1(type)) {
                                                                                                        return A1;
                                                                                                    } else if (checkCompatibleP3(type)) {
                                                                                                        return P3;
                                                                                                    } else if (checkCompatibleP3Pro(type)) {
                                                                                                        return P3Pro;
                                                                                                    } else if (checkCompatibleP1(type)) {
                                                                                                        return P1;
                                                                                                    } else if (checkCompatibleP2(type)) {
                                                                                                        return P2;
                                                                                                    } else if (checkCompatibleP4(type)) {
                                                                                                        return P4;
                                                                                                    } else if (checkCompatibleK1(type)) {
                                                                                                        return K1;
                                                                                                    } else if (checkCompatibleK2(type)) {
                                                                                                        return K2;
                                                                                                    } else if (checkCompatibleQ1(type)) {
                                                                                                        return Q1;
                                                                                                    } else if (checkCompatibleT1(type)) {
                                                                                                        return T1;
                                                                                                    } else if (checkCompatibleM1(type)) {
                                                                                                        return M1;
                                                                                                    } else if (checkCompatibleW0_CAM(type)) {
                                                                                                        return W0_CAM;
                                                                                                    } else if (checkCompatibleW0_HUB(type)) {
                                                                                                        return W0_HUB;
                                                                                                    } else if (checkCompatibleW1(type)) {
                                                                                                        return W1;
                                                                                                    } else if (checkCompatibleW2(type)) {
                                                                                                        return W2;
                                                                                                    } else {
                                                                                                        return checkCompatibleXc100(type) ? XC100 : IPC_UNKNOWN;
                                                                                                    }
                                                                                                } else {
                                                                                                    return TS100;
                                                                                                }
                                                                                            } else {
                                                                                                return TR100;
                                                                                            }
                                                                                        } else {
                                                                                            return TC100;
                                                                                        }
                                                                                    } else {
                                                                                        return MC120;
                                                                                    }
                                                                                } else {
                                                                                    return EC810_HUB;
                                                                                }
                                                                            } else {
                                                                                return EC810_CAM;
                                                                            }
                                                                        } else {
                                                                            return EC810PRO;
                                                                        }
                                                                    } else {
                                                                        return EC810_PLUS;
                                                                    }
                                                                } else {
                                                                    return SC220;
                                                                }
                                                            } else {
                                                                return SC210;
                                                            }
                                                        } else {
                                                            return PC770;
                                                        }
                                                    } else {
                                                        return PC730;
                                                    }
                                                } else {
                                                    return PC650;
                                                }
                                            } else {
                                                return PC540;
                                            }
                                        } else {
                                            return PC530;
                                        }
                                    } else {
                                        return PC440;
                                    }
                                } else {
                                    return PC420;
                                }
                            } else {
                                return IPC_300_HUB;
                            }
                        } else {
                            return IPC_300_CAM;
                        }
                    } else {
                        return IPC_200;
                    }
                } else {
                    return IPC_100;
                }
            } else {
                return IPC_1080;
            }
        } else {
            return IPC_720;
        }
    }

    public static boolean checkCompatibleIpc720(String compatibleType) {
        return checkCompatibleType("IPC007-720P", compatibleType);
    }

    public static boolean checkCompatibleIpc1080(String compatibleType) {
        return checkCompatibleType("IPC007-1080P", compatibleType);
    }

    public static boolean checkCompatibleIpc100(String compatibleType) {
        return checkCompatibleType("IPC100", compatibleType);
    }

    public static boolean checkCompatibleIpc200(String compatibleType) {
        return checkCompatibleType("IPC200", compatibleType);
    }

    public static boolean checkCompatibleIpc300Cam(String compatibleType) {
        return checkCompatibleType("IPC300-CAM", compatibleType);
    }

    public static boolean checkCompatibleIpc300Hub(String compatibleType) {
        return checkCompatibleType("IPC300-HUB", compatibleType);
    }

    public static boolean checkCompatiblePc420(String compatibleType) {
        return checkCompatibleType("PC420", compatibleType);
    }

    public static boolean checkCompatiblePc440(String compatibleType) {
        return checkCompatibleType("PC440", compatibleType);
    }

    public static boolean checkCompatiblePc530(String compatibleType) {
        return checkCompatibleType("PC530", compatibleType);
    }

    public static boolean checkCompatiblePc530Pro(String compatibleType) {
        return checkCompatibleType("PC530PRO", compatibleType);
    }

    public static boolean checkCompatiblePc540(String compatibleType) {
        return checkCompatibleType("PC540", compatibleType);
    }

    public static boolean checkCompatiblePc650(String compatibleType) {
        return checkCompatibleType("PC650", compatibleType);
    }

    public static boolean checkCompatiblePc660(String compatibleType) {
        return checkCompatibleType("PC660", compatibleType);
    }

    public static boolean checkCompatiblePc660Pro(String compatibleType) {
        return checkCompatibleType("PC660PRO", compatibleType);
    }

    public static boolean checkCompatiblePc730(String compatibleType) {
        return checkCompatibleType("PC730", compatibleType);
    }

    public static boolean checkCompatiblePc770(String compatibleType) {
        return checkCompatibleType("PC770", compatibleType);
    }

    public static boolean checkCompatibleSc100(String compatibleType) {
        return checkCompatibleType("SC100", compatibleType);
    }

    public static boolean checkCompatibleSc210(String compatibleType) {
        return checkCompatibleType("SC210", compatibleType);
    }

    public static boolean checkCompatibleSc220(String compatibleType) {
        return checkCompatibleType("SC220", compatibleType);
    }

    public static boolean checkCompatibleMc120(String compatibleType) {
        return checkCompatibleType("MC120", compatibleType);
    }

    public static boolean checkCompatibleTc100(String compatibleType) {
        return checkCompatibleType("TC100", compatibleType);
    }

    public static boolean checkCompatibleTs100(String compatibleType) {
        return checkCompatibleType("TS100", compatibleType);
    }

    public static boolean checkCompatibleTs200(String compatibleType) {
        return checkCompatibleType("TS200", compatibleType);
    }

    public static boolean checkCompatibleTr100(String compatibleType) {
        return checkCompatibleType("TR100", compatibleType);
    }

    public static boolean checkCompatibleHc320(String compatibleType) {
        return checkCompatibleType("HC320", compatibleType);
    }

    public static boolean checkCompatibleEc810Cam(String compatibleType) {
        return checkCompatibleType("EC810-CAM", compatibleType);
    }

    public static boolean checkCompatibleEc810Hub(String compatibleType) {
        return checkCompatibleType("EC810-HUB", compatibleType);
    }

    public static boolean checkCompatibleEc810Pro(String compatibleType) {
        return checkCompatibleType("EC810PRO", compatibleType);
    }

    public static boolean checkCompatibleEc810Plus(String compatibleType) {
        return checkCompatibleType("EC810-PLUS", compatibleType);
    }

    public static boolean checkCompatibleC1(String compatibleType) {
        return checkCompatibleType("C1", compatibleType);
    }

    public static boolean checkCompatibleA1(String compatibleType) {
        return checkCompatibleType("C2", compatibleType);
    }

    public static boolean checkCompatibleP3(String compatibleType) {
        return checkCompatibleType("P3", compatibleType);
    }

    public static boolean checkCompatibleP3Pro(String compatibleType) {
        return checkCompatibleType("P3Pro", compatibleType);
    }

    public static boolean checkCompatibleP1(String compatibleType) {
        return checkCompatibleType("P1", compatibleType);
    }

    public static boolean checkCompatibleP2(String compatibleType) {
        return checkCompatibleType("P2", compatibleType);
    }

    public static boolean checkCompatibleP4(String compatibleType) {
        return checkCompatibleType("P4", compatibleType);
    }

    public static boolean checkCompatibleK1(String compatibleType) {
        return checkCompatibleType("K1", compatibleType);
    }

    public static boolean checkCompatibleK2(String compatibleType) {
        return checkCompatibleType("K2", compatibleType);
    }

    public static boolean checkCompatibleQ1(String compatibleType) {
        return checkCompatibleType("Q1", compatibleType);
    }

    public static boolean checkCompatibleT1(String compatibleType) {
        return checkCompatibleType("T1", compatibleType);
    }

    public static boolean checkCompatibleM1(String compatibleType) {
        return checkCompatibleType("M1", compatibleType);
    }

    public static boolean checkCompatibleW0_CAM(String compatibleType) {
        return checkCompatibleType("W0-CAM", compatibleType);
    }

    public static boolean checkCompatibleW0_HUB(String compatibleType) {
        return checkCompatibleType("W1-HUB", compatibleType);
    }

    public static boolean checkCompatibleW1(String compatibleType) {
        return checkCompatibleType("W1-CAM", compatibleType);
    }

    public static boolean checkCompatibleW2(String compatibleType) {
        return checkCompatibleType("W2", compatibleType);
    }

    public static boolean checkCompatibleXc100(String compatibleType) {
        return checkCompatibleType("XC100", compatibleType);
    }

    private static boolean checkCompatibleType(List<String> defineTypes, String compatibleType) {
        List<String> compatibleFactors = new ArrayList();
        compatibleFactors.add("A");
        compatibleFactors.add("F");
        return checkCompatibleType(defineTypes, compatibleType, compatibleFactors);
    }

    private static boolean checkCompatibleType(List<String> defineTypes, String compatibleType, List<String> compatibleFactors) {
        if (defineTypes != null && !defineTypes.isEmpty() && !TextUtils.isEmpty(compatibleType)) {
            boolean isCompatibleType = false;
            Iterator var4 = defineTypes.iterator();

            while(var4.hasNext()) {
                String defineType = (String)var4.next();
                if (!TextUtils.isEmpty(defineType) && compatibleType.contains(defineType)) {
                    isCompatibleType = true;
                    break;
                }
            }

            if (!isCompatibleType) {
                return false;
            } else if (compatibleFactors != null && !compatibleFactors.isEmpty()) {
                boolean isCompatibleFactor = false;
                Iterator var8 = compatibleFactors.iterator();

                while(var8.hasNext()) {
                    String compatibleFactor = (String)var8.next();
                    if (compatibleType.contains(compatibleFactor)) {
                        isCompatibleFactor = true;
                        break;
                    }
                }

                return isCompatibleFactor;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    private static boolean checkCompatibleType(String defineType, String compatibleType) {
        return !TextUtils.isEmpty(defineType) && !TextUtils.isEmpty(compatibleType) && compatibleType.startsWith(defineType);
    }
}
