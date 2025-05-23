% Generated by roxygen2: do not edit by hand
% Please edit documentation in R/zgcamusa_L2238.PV_reeds.R
\name{module_gcamusa_L2238.PV_reeds}
\alias{module_gcamusa_L2238.PV_reeds}
\title{module_gcamusa_L2238.PV_reeds}
\usage{
module_gcamusa_L2238.PV_reeds(command, ...)
}
\arguments{
\item{command}{API command to execute}

\item{...}{other optional parameters, depending on command}
}
\value{
Depends on \code{command}: either a vector of required inputs,
a vector of output names, or (if \code{command} is "MAKE") all
the generated outputs: \code{L2238.DeleteStubTechMinicamEnergyInput_PV_reeds_USA}, \code{L2238.RenewRsrc_PV_reeds_USA},
\code{L2238.GrdRenewRsrcCurves_PV_reeds_USA}, \code{L2238.GrdRenewRsrcMax_PV_reeds_USA},
\code{L2238.StubTechEffFlag_PV_reeds_USA}, \code{L2238.StubTechCapFactor_PV_reeds_USA},
\code{L2238.RenewRsrcTechChange_PV_reeds_USA}, and \code{L2238.StubTechCost_PV_reeds_USA},
\code{L2238.ResTechShrwt_PV_reeds_USA}.
The corresponding file in the original data system was \code{L2238.PV_reeds_USA.R} (gcam-usa level2).
}
\description{
Create updated solar PV resource supply curves consistent with ReEDS.
Also add non-ReEDS states (AK,DC,HI) based on NREL technical potential data.
}
\details{
Create state-level solar PV resource supply curves
}
\author{
MTB September 2018; AJS June 2019
}
