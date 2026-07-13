package it.application.nationaldefencemanagementsystem.Entities;

public enum EquipmentStatus {
    ACTIVE,          // Currently assigned to an operator and in use
    IN_STORAGE,      // Available in the warehouse/armory
    IN_MAINTENANCE,  // Undergoing repairs or routine inspections
    MISSING,         // Lost in action or unaccounted for
    DECOMMISSIONED   // Permanently retired from service
}
